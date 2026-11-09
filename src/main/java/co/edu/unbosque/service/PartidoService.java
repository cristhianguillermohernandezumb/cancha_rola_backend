package co.edu.unbosque.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.PartidoRequest;
import co.edu.unbosque.dto.PartidoResponse;
import co.edu.unbosque.entity.Cancha;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.PartidoMapper;
import co.edu.unbosque.repository.AlineacionRepository;
import co.edu.unbosque.repository.EventoPartidoRepository;
import co.edu.unbosque.repository.ParArbRepository;
import co.edu.unbosque.repository.ParEquRepository;
import co.edu.unbosque.repository.PartidoRepository;
import co.edu.unbosque.repository.PenalRepository;

@Service
public class PartidoService {

	private final Autorizacion autorizacion;

	private static final String PROGRAMADO = "PROGRAMADO";
	private static final String EN_JUEGO = "EN_JUEGO";
	private static final String FINALIZADO = "FINALIZADO";
	private static final String APLAZADO = "APLAZADO";

	private static final Map<String, List<String>> TRANSICIONES = Map.of(
			PROGRAMADO, List.of(EN_JUEGO, APLAZADO),
			APLAZADO, List.of(PROGRAMADO),
			EN_JUEGO, List.of(FINALIZADO),
			FINALIZADO, List.of());

	private final PartidoRepository partidoRepository;
	private final ParEquRepository parEquRepository;
	private final ParArbRepository parArbRepository;
	private final AlineacionRepository alineacionRepository;
	private final EventoPartidoRepository eventoPartidoRepository;
	private final PenalRepository penalRepository;
	private final TorneoService torneoService;
	private final CanchaService canchaService;
	private final PartidoMapper partidoMapper;

	public PartidoService(PartidoRepository partidoRepository, ParEquRepository parEquRepository,
			ParArbRepository parArbRepository, PenalRepository penalRepository, TorneoService torneoService, CanchaService canchaService,
			PartidoMapper partidoMapper, Autorizacion autorizacion, AlineacionRepository alineacionRepository,
			EventoPartidoRepository eventoPartidoRepository) {
		this.alineacionRepository = alineacionRepository;
		this.eventoPartidoRepository = eventoPartidoRepository;
		this.autorizacion = autorizacion;
		this.partidoRepository = partidoRepository;
		this.parEquRepository = parEquRepository;
		this.parArbRepository = parArbRepository;
		this.penalRepository = penalRepository;
		this.torneoService = torneoService;
		this.canchaService = canchaService;
		this.partidoMapper = partidoMapper;
	}

	@Transactional
	public PartidoResponse crear(PartidoRequest request) {
		Torneo torneo = torneoService.buscarEntidad(request.idTorneo());
		autorizacion.exigirDuenoDeTorneo(torneo);
		if (FINALIZADO.equals(torneo.getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo, no se pueden programar partidos");
		}
		validarFechaEnTorneo(request.fechaHora(), torneo);
		Cancha cancha = canchaService.buscarEntidad(request.idCancha());
		validarCanchaLibre(cancha.getIdCancha(), request.fechaHora());
		Partido partido = partidoMapper.toEntity(request, cancha, torneo);
		if (partido.getEstado() == null) {
			partido.setEstado(PROGRAMADO);
		}
		return partidoMapper.toResponse(partidoRepository.save(partido));
	}

	@Transactional(readOnly = true)
	public List<PartidoResponse> listar(Long idTorneo, Integer jornada, String estado) {
		List<Partido> partidos;
		if (idTorneo == null) {
			partidos = partidoRepository.findAll();
		} else {
			torneoService.buscarEntidad(idTorneo);
			if (jornada != null) {
				partidos = partidoRepository.findByTorneo_IdTorneoAndJornada(idTorneo, jornada);
			} else if (estado != null && !estado.isBlank()) {
				partidos = partidoRepository.findByTorneo_IdTorneoAndEstado(idTorneo, estado.toUpperCase());
			} else {
				partidos = partidoRepository.findByTorneo_IdTorneo(idTorneo);
			}
		}
		return partidos.stream().map(partidoMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public PartidoResponse obtener(Long idPartido) {
		return partidoMapper.toResponse(buscarEntidad(idPartido));
	}

	@Transactional
	public PartidoResponse actualizar(Long idPartido, PartidoRequest request) {
		Partido partido = buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		validarEditable(partido);
		if (!partido.getTorneo().getIdTorneo().equals(request.idTorneo())) {
			throw new ReglaNegocioException("No se puede cambiar el torneo de un partido");
		}
		validarFechaEnTorneo(request.fechaHora(), partido.getTorneo());
		Cancha cancha = canchaService.buscarEntidad(request.idCancha());
		boolean cambioSitio = !partido.getCancha().getIdCancha().equals(cancha.getIdCancha())
				|| !partido.getFechaHora().equals(request.fechaHora());
		if (cambioSitio) {
			validarCanchaLibre(cancha.getIdCancha(), request.fechaHora());
		}
		if (request.estado() != null && !request.estado().equals(partido.getEstado())) {
			throw new ReglaNegocioException("Para cambiar el estado use PATCH /partidos/{id}/estado");
		}
		partidoMapper.actualizarEntidad(partido, request, cancha);
		return partidoMapper.toResponse(partidoRepository.save(partido));
	}

	@Transactional
	public PartidoResponse cambiarEstado(Long idPartido, String nuevoEstado) {
		String destino = nuevoEstado == null ? "" : nuevoEstado.toUpperCase();
		Partido partido = buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		List<String> permitidos = TRANSICIONES.getOrDefault(partido.getEstado(), List.of());
		if (!permitidos.contains(destino)) {
			throw new ReglaNegocioException("No se puede pasar de " + partido.getEstado() + " a "
					+ (destino.isBlank() ? "(vacio)" : destino) + ". Permitidos: "
					+ (permitidos.isEmpty() ? "ninguno" : String.join(", ", permitidos)));
		}
		if (EN_JUEGO.equals(destino)) {
			validarListoParaJugar(partido);
		}
		if (FINALIZADO.equals(destino)) {
			validarTandaSiEmpate(partido);
		}
		partido.setEstado(destino);
		return partidoMapper.toResponse(partidoRepository.save(partido));
	}

	@Transactional
	public void eliminar(Long idPartido) {
		Partido partido = buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		validarEditable(partido);
		eventoPartidoRepository.deleteAll(eventoPartidoRepository.findByIdPartidoOrderByMinutoAsc(idPartido));
		alineacionRepository.deleteAll(alineacionRepository.buscarPorPartido(idPartido));
		alineacionRepository.flush();
		parEquRepository.deleteAll(parEquRepository.buscarPorPartido(idPartido));
		parArbRepository.deleteAll(parArbRepository.buscarPorPartido(idPartido));
		penalRepository.deleteAll(penalRepository.findByIdPartidoOrderByOrdenAsc(idPartido));
		partidoRepository.delete(partido);
	}

	@Transactional(readOnly = true)
	public Partido buscarEntidad(Long idPartido) {
		return partidoRepository.findById(idPartido)
				.orElseThrow(() -> new NoEncontradoException("No existe el partido con id " + idPartido));
	}

	private void validarTandaSiEmpate(Partido partido) {
		if ("Grupos".equalsIgnoreCase(partido.getFase())
				|| !partido.getGolesLocal().equals(partido.getGolesVisitante())) {
			return;
		}
		List<Boolean> tiros = penalRepository.findByIdPartidoOrderByOrdenAsc(partido.getIdPartido()).stream()
				.map(t -> "S".equals(t.getConvertido())).toList();
		if (TandaPenales.calcular(tiros).ganador() == null) {
			throw new ReglaNegocioException("El partido de eliminatoria va empatado: registra la tanda de penales "
					+ "hasta que haya un ganador antes de finalizarlo");
		}
	}

	private void validarEditable(Partido partido) {
		if (!PROGRAMADO.equals(partido.getEstado()) && !APLAZADO.equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se puede modificar un partido PROGRAMADO o APLAZADO (estado actual: "
					+ partido.getEstado() + ")");
		}
	}

	private void validarFechaEnTorneo(LocalDateTime fechaHora, Torneo torneo) {
		LocalDate dia = fechaHora.toLocalDate();
		if (dia.isBefore(torneo.getFechaInicio()) || dia.isAfter(torneo.getFechaFin())) {
			throw new ReglaNegocioException("La fecha del partido debe estar entre " + torneo.getFechaInicio()
					+ " y " + torneo.getFechaFin() + " (fechas del torneo)");
		}
	}

	private void validarCanchaLibre(Long idCancha, LocalDateTime fechaHora) {
		if (partidoRepository.existsByCancha_IdCanchaAndFechaHora(idCancha, fechaHora)) {
			throw new ReglaNegocioException("La cancha ya tiene un partido programado en esa fecha y hora");
		}
	}

	private void validarListoParaJugar(Partido partido) {
		if (!"EN_CURSO".equals(partido.getTorneo().getEstado())) {
			throw new ReglaNegocioException("El torneo debe estar EN_CURSO para iniciar partidos (estado: "
					+ partido.getTorneo().getEstado() + ")");
		}
		if (parEquRepository.contarPorPartido(partido.getIdPartido()) != 2) {
			throw new ReglaNegocioException("El partido necesita exactamente 2 equipos (LOCAL y VISITANTE)");
		}
		if (!parArbRepository.existeRolEnPartido(partido.getIdPartido(), "CENTRAL")) {
			throw new ReglaNegocioException("El partido necesita un arbitro CENTRAL");
		}
		int minimo = AlineacionService.TITULARES.getOrDefault(partido.getTorneo().getModalidad(), 11);
		for (var equipo : parEquRepository.buscarPorPartido(partido.getIdPartido())) {
			long titulares = alineacionRepository.contarTitulares(partido.getIdPartido(), equipo.getId().getIdEquipo());
			if (titulares < minimo) {
				throw new ReglaNegocioException("La alineacion del equipo " + equipo.getRol() + " esta incompleta: tiene "
						+ titulares + " titulares y se necesitan " + minimo + " para iniciar el partido");
			}
		}
	}

}
