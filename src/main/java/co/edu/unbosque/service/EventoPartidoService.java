package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.EventoPartidoRequest;
import co.edu.unbosque.dto.EventoPartidoResponse;
import co.edu.unbosque.entity.Alineacion;
import co.edu.unbosque.entity.EventoPartido;
import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.EventoPartidoMapper;
import co.edu.unbosque.repository.EventoPartidoRepository;
import co.edu.unbosque.repository.ParEquRepository;
import co.edu.unbosque.repository.PartidoRepository;

/**
 * Goles, tarjetas y faltas de un partido. Cada gol o autogol recalcula el marcador del partido, asi que
 * goles_local y goles_visitante siempre coinciden con los eventos registrados.
 */
@Service
public class EventoPartidoService {

	private final Autorizacion autorizacion;

	private static final int MINUTO_MAXIMO = 120;

	private final EventoPartidoRepository eventoRepository;
	private final PartidoRepository partidoRepository;
	private final ParEquRepository parEquRepository;
	private final PartidoService partidoService;
	private final AlineacionService alineacionService;
	private final EventoPartidoMapper eventoMapper;

	public EventoPartidoService(EventoPartidoRepository eventoRepository, PartidoRepository partidoRepository,
			ParEquRepository parEquRepository, PartidoService partidoService, AlineacionService alineacionService,
			EventoPartidoMapper eventoMapper, Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.eventoRepository = eventoRepository;
		this.partidoRepository = partidoRepository;
		this.parEquRepository = parEquRepository;
		this.partidoService = partidoService;
		this.alineacionService = alineacionService;
		this.eventoMapper = eventoMapper;
	}

	@Transactional
	public EventoPartidoResponse registrar(EventoPartidoRequest request) {
		Partido partido = partidoService.buscarEntidad(request.idPartido());
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if (!"EN_JUEGO".equals(partido.getEstado())) {
			throw new ReglaNegocioException(
					"Solo se registran eventos en partidos EN_JUEGO (estado actual: " + partido.getEstado() + ")");
		}
		if (!partido.getTorneo().getIdTorneo().equals(request.idTorneo())) {
			throw new ReglaNegocioException("El torneo indicado no es el del partido");
		}
		if (request.minuto() > MINUTO_MAXIMO) {
			throw new ReglaNegocioException("El minuto debe estar entre 0 y " + MINUTO_MAXIMO);
		}
		Alineacion alineacion = alineacionService.buscarEntidad(partido, request.idEquipo(), request.idJugador());
		validarSinExpulsion(request.idPartido(), request.idJugador());

		EventoPartido evento = eventoMapper.toEntity(request);
		EventoPartido guardado = eventoRepository.save(evento);
		guardado.setAlineacion(alineacion);
		if (afectaMarcador(guardado.getTipoEvento())) {
			recalcularMarcador(partido);
		}
		return eventoMapper.toResponse(guardado);
	}

	@Transactional(readOnly = true)
	public List<EventoPartidoResponse> listarPorPartido(Long idPartido) {
		partidoService.buscarEntidad(idPartido);
		return eventoRepository.findByIdPartidoOrderByMinutoAsc(idPartido).stream().map(eventoMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public EventoPartidoResponse obtener(Long idEvento) {
		return eventoMapper.toResponse(buscarEntidad(idEvento));
	}

	@Transactional
	public void eliminar(Long idEvento) {
		EventoPartido evento = buscarEntidad(idEvento);
		Partido partido = partidoService.buscarEntidad(evento.getIdPartido());
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if (!"EN_JUEGO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se eliminan eventos de partidos EN_JUEGO");
		}
		eventoRepository.delete(evento);
		if (afectaMarcador(evento.getTipoEvento())) {
			recalcularMarcador(partido);
		}
	}

	@Transactional(readOnly = true)
	public EventoPartido buscarEntidad(Long idEvento) {
		return eventoRepository.findById(idEvento)
				.orElseThrow(() -> new NoEncontradoException("No existe el evento con id " + idEvento));
	}

	private boolean afectaMarcador(String tipo) {
		return "GOL".equals(tipo) || "AUTOGOL".equals(tipo);
	}

	private void validarSinExpulsion(Long idPartido, Long idJugador) {
		boolean expulsado = eventoRepository.findByIdPartidoAndIdJugador(idPartido, idJugador).stream()
				.anyMatch(e -> "ROJA".equals(e.getTipoEvento()));
		if (expulsado) {
			throw new ReglaNegocioException("El jugador ya fue expulsado en este partido");
		}
	}

	private void recalcularMarcador(Partido partido) {
		Long idPartido = partido.getIdPartido();
		Long idLocal = null;
		Long idVisitante = null;
		for (ParEqu pe : parEquRepository.buscarPorPartido(idPartido)) {
			if ("LOCAL".equals(pe.getRol())) {
				idLocal = pe.getId().getIdEquipo();
			} else {
				idVisitante = pe.getId().getIdEquipo();
			}
		}
		if (idLocal == null || idVisitante == null) {
			throw new ReglaNegocioException("El partido no tiene sus dos equipos asignados");
		}
		long golesLocal = eventoRepository.contarPorPartidoEquipoYTipo(idPartido, idLocal, "GOL")
				+ eventoRepository.contarPorPartidoEquipoYTipo(idPartido, idVisitante, "AUTOGOL");
		long golesVisitante = eventoRepository.contarPorPartidoEquipoYTipo(idPartido, idVisitante, "GOL")
				+ eventoRepository.contarPorPartidoEquipoYTipo(idPartido, idLocal, "AUTOGOL");
		partido.setGolesLocal((int) golesLocal);
		partido.setGolesVisitante((int) golesVisitante);
		partidoRepository.save(partido);
	}

}
