package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.ParArbResponse;
import co.edu.unbosque.entity.Arbitro;
import co.edu.unbosque.entity.ParArb;
import co.edu.unbosque.entity.ParArbId;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.ParArbMapper;
import co.edu.unbosque.repository.ParArbRepository;

/** Los arbitros de un partido (central, asistentes, cuarto, VAR). */
@Service
public class ParArbService {

	private final Autorizacion autorizacion;

	private static final List<String> ROLES = List.of("CENTRAL", "ASISTENTE_1", "ASISTENTE_2", "CUARTO", "VAR");

	private final ParArbRepository parArbRepository;
	private final PartidoService partidoService;
	private final ArbitroService arbitroService;
	private final ParArbMapper parArbMapper;

	public ParArbService(ParArbRepository parArbRepository, PartidoService partidoService,
			ArbitroService arbitroService, ParArbMapper parArbMapper, Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.parArbRepository = parArbRepository;
		this.partidoService = partidoService;
		this.arbitroService = arbitroService;
		this.parArbMapper = parArbMapper;
	}

	@Transactional
	public ParArbResponse asignar(Long idPartido, Long idArbitro, String rol) {
		String rolNormal = rol == null ? "" : rol.toUpperCase();
		if (!ROLES.contains(rolNormal)) {
			throw new ReglaNegocioException("Rol invalido. Valores permitidos: " + String.join(", ", ROLES));
		}
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if (!"PROGRAMADO".equals(partido.getEstado()) && !"APLAZADO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se asignan arbitros a partidos PROGRAMADO o APLAZADO");
		}
		Arbitro arbitro = arbitroService.buscarEntidad(idArbitro);
		if (!"ACTIVO".equals(arbitro.getEstado())) {
			throw new ReglaNegocioException("El arbitro esta INACTIVO y no se puede asignar");
		}
		ParArbId id = new ParArbId(idPartido, idArbitro);
		if (parArbRepository.existsById(id)) {
			throw new ReglaNegocioException("El arbitro ya esta asignado a este partido");
		}
		if (parArbRepository.existeRolEnPartido(idPartido, rolNormal)) {
			throw new ReglaNegocioException("El partido ya tiene un arbitro con el rol " + rolNormal);
		}
		if (parArbRepository.existeArbitroEnFecha(idArbitro, idPartido, partido.getFechaHora())) {
			throw new ReglaNegocioException("El arbitro ya tiene otro partido a esa misma fecha y hora");
		}
		ParArb parArb = new ParArb();
		parArb.setId(id);
		parArb.setRol(rolNormal);
		ParArb guardado = parArbRepository.save(parArb);
		guardado.setPartido(partido);
		guardado.setArbitro(arbitro);
		return parArbMapper.toResponse(guardado);
	}

	@Transactional(readOnly = true)
	public List<ParArbResponse> listarPorPartido(Long idPartido) {
		partidoService.buscarEntidad(idPartido);
		return parArbRepository.buscarPorPartido(idPartido).stream().map(parArbMapper::toResponse).toList();
	}

	@Transactional
	public void quitar(Long idPartido, Long idArbitro) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if (!"PROGRAMADO".equals(partido.getEstado()) && !"APLAZADO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se quitan arbitros de partidos PROGRAMADO o APLAZADO");
		}
		ParArbId id = new ParArbId(idPartido, idArbitro);
		if (!parArbRepository.existsById(id)) {
			throw new NoEncontradoException("El arbitro " + idArbitro + " no esta asignado al partido " + idPartido);
		}
		parArbRepository.deleteById(id);
	}

}
