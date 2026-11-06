package co.edu.unbosque.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.TorneoRequest;
import co.edu.unbosque.dto.TorneoResponse;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.event.TorneoCambioEstadoEvent;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.TorneoMapper;
import co.edu.unbosque.repository.EquTorRepository;
import co.edu.unbosque.repository.TorneoRepository;

@Service
public class TorneoService {

	private final Autorizacion autorizacion;

	private static final String INSCRIPCION = "INSCRIPCION";
	private static final String EN_CURSO = "EN_CURSO";
	private static final String FINALIZADO = "FINALIZADO";
	private static final List<String> ETAPAS = List.of(INSCRIPCION, EN_CURSO, FINALIZADO);
	private static final int MIN_EQUIPOS_PARA_INICIAR = 2;
	private static final List<Integer> TAMANOS_PERMITIDOS = List.of(24, 48);

	private final TorneoRepository torneoRepository;
	private final EquTorRepository equTorRepository;
	private final TorneoMapper torneoMapper;
	private final UsuarioService usuarioService;
	private final ApplicationEventPublisher eventos;

	public TorneoService(TorneoRepository torneoRepository, EquTorRepository equTorRepository,
			TorneoMapper torneoMapper, UsuarioService usuarioService, Autorizacion autorizacion,
			ApplicationEventPublisher eventos) {
		this.eventos = eventos;
		this.autorizacion = autorizacion;
		this.torneoRepository = torneoRepository;
		this.equTorRepository = equTorRepository;
		this.torneoMapper = torneoMapper;
		this.usuarioService = usuarioService;
	}

	@Transactional
	public TorneoResponse crear(TorneoRequest request) {
		Usuario organizador = usuarioService.buscarEntidad(request.idOrganizador());
		autorizacion.exigirUsuarioPropio(request.idOrganizador());
		if (!"ORGANIZADOR".equals(organizador.getRol()) || !"ACTIVO".equals(organizador.getEstado())) {
			throw new ReglaNegocioException("Solo un usuario ORGANIZADOR activo puede crear torneos");
		}
		validarFechas(request);
		validarTamano(request.maxEquipos());
		if (torneoRepository.existsByNombreIgnoreCaseAndOrganizador_IdUsuario(request.nombre(),
				organizador.getIdUsuario())) {
			throw new ReglaNegocioException("Ya tienes un torneo llamado " + request.nombre());
		}
		Torneo torneo = torneoMapper.toEntity(request, organizador);
		torneo.setEstado(INSCRIPCION);
		return torneoMapper.toResponse(torneoRepository.save(torneo));
	}

	@Transactional(readOnly = true)
	public List<TorneoResponse> listar(String estado) {
		List<Torneo> torneos = (estado == null || estado.isBlank()) ? torneoRepository.findAll()
				: torneoRepository.findByEstado(estado.toUpperCase());
		return torneos.stream().map(torneoMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<TorneoResponse> listarPorOrganizador(Long idUsuario) {
		return torneoRepository.findByOrganizador_IdUsuario(idUsuario).stream().map(torneoMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public TorneoResponse obtener(Long idTorneo) {
		return torneoMapper.toResponse(buscarEntidad(idTorneo));
	}

	@Transactional
	public TorneoResponse actualizar(Long idTorneo, TorneoRequest request) {
		Torneo torneo = buscarEntidad(idTorneo);
		autorizacion.exigirDuenoDeTorneo(torneo);
		if (FINALIZADO.equals(torneo.getEstado())) {
			throw new ReglaNegocioException("Un torneo FINALIZADO ya no se puede modificar");
		}
		if (!torneo.getOrganizador().getIdUsuario().equals(request.idOrganizador())) {
			throw new ReglaNegocioException("No se puede cambiar el organizador de un torneo");
		}
		validarFechas(request);
		if (!request.maxEquipos().equals(torneo.getMaxEquipos())) {
			validarTamano(request.maxEquipos());
		}
		boolean cambioNombre = !torneo.getNombre().equalsIgnoreCase(request.nombre());
		if (cambioNombre && torneoRepository.existsByNombreIgnoreCaseAndOrganizador_IdUsuario(request.nombre(),
				request.idOrganizador())) {
			throw new ReglaNegocioException("Ya tienes un torneo llamado " + request.nombre());
		}
		long inscritos = equTorRepository.contarPorTorneo(idTorneo);
		if (request.maxEquipos() < inscritos) {
			throw new ReglaNegocioException("El torneo ya tiene " + inscritos
					+ " equipos inscritos, el maximo no puede ser menor");
		}
		torneoMapper.actualizarEntidad(torneo, request);
		return torneoMapper.toResponse(torneoRepository.save(torneo));
	}

	@Transactional
	public TorneoResponse cambiarEstado(Long idTorneo, String nuevoEstado) {
		Torneo torneo = buscarEntidad(idTorneo);
		autorizacion.exigirDuenoDeTorneo(torneo);
		String destino = nuevoEstado == null ? "" : nuevoEstado.toUpperCase();
		int actual = ETAPAS.indexOf(torneo.getEstado());
		int siguiente = ETAPAS.indexOf(destino);
		if (siguiente < 0) {
			throw new ReglaNegocioException("Estado invalido. Valores permitidos: " + String.join(", ", ETAPAS));
		}
		if (siguiente != actual + 1) {
			throw new ReglaNegocioException(
					"No se puede pasar de " + torneo.getEstado() + " a " + destino + ". El orden es " + String.join(" -> ", ETAPAS));
		}
		if (EN_CURSO.equals(destino)) {
			long inscritos = equTorRepository.contarPorTorneo(idTorneo);
			if (inscritos < MIN_EQUIPOS_PARA_INICIAR) {
				throw new ReglaNegocioException("Para iniciar el torneo se necesitan al menos "
						+ MIN_EQUIPOS_PARA_INICIAR + " equipos inscritos (hay " + inscritos + ")");
			}
		}
		torneo.setEstado(destino);
		Torneo guardado = torneoRepository.save(torneo);
		eventos.publishEvent(new TorneoCambioEstadoEvent(idTorneo, destino));
		return torneoMapper.toResponse(guardado);
	}

	@Transactional(readOnly = true)
	public Torneo buscarEntidad(Long idTorneo) {
		return torneoRepository.findById(idTorneo)
				.orElseThrow(() -> new NoEncontradoException("No existe el torneo con id " + idTorneo));
	}

	private void validarTamano(Integer maxEquipos) {
		if (!TAMANOS_PERMITIDOS.contains(maxEquipos)) {
			throw new ReglaNegocioException("El torneo debe ser de 24 equipos (6 grupos) o de 48 equipos (12 grupos)");
		}
	}

	private void validarFechas(TorneoRequest request) {
		if (request.fechaFin().isBefore(request.fechaInicio())) {
			throw new ReglaNegocioException("La fecha de fin no puede ser anterior a la fecha de inicio");
		}
	}

}
