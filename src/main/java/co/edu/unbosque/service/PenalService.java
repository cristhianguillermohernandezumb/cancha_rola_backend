package co.edu.unbosque.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.PenalesResponse;
import co.edu.unbosque.dto.TiroPenalResponse;
import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.entity.Penal;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.repository.ParEquRepository;
import co.edu.unbosque.repository.PartidoRepository;
import co.edu.unbosque.repository.PenalRepository;
import co.edu.unbosque.security.Autorizacion;

/** Tanda de penales de los partidos de eliminatoria que terminan empatados (reglas en TandaPenales). */
@Service
public class PenalService {

	private final Autorizacion autorizacion;
	private final PenalRepository penalRepository;
	private final ParEquRepository parEquRepository;
	private final PartidoRepository partidoRepository;
	private final PartidoService partidoService;
	private final TorneoService torneoService;

	public PenalService(PenalRepository penalRepository, ParEquRepository parEquRepository,
			PartidoRepository partidoRepository, PartidoService partidoService, TorneoService torneoService,
			Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.penalRepository = penalRepository;
		this.parEquRepository = parEquRepository;
		this.partidoRepository = partidoRepository;
		this.partidoService = partidoService;
		this.torneoService = torneoService;
	}

	/** Consulta publica de la tanda de un partido. */
	@Transactional(readOnly = true)
	public PenalesResponse obtener(Long idPartido) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		return armar(partido, penalRepository.findByIdPartidoOrderByOrdenAsc(idPartido));
	}

	/** Consulta publica: las tandas de todos los partidos del torneo que tienen penales. */
	@Transactional(readOnly = true)
	public List<PenalesResponse> listarDeTorneo(Long idTorneo) {
		torneoService.buscarEntidad(idTorneo);
		List<Partido> partidos = partidoRepository.findByTorneo_IdTorneo(idTorneo);
		if (partidos.isEmpty()) {
			return List.of();
		}
		Map<Long, List<Penal>> porPartido = penalRepository
				.buscarDePartidos(partidos.stream().map(Partido::getIdPartido).toList()).stream()
				.collect(Collectors.groupingBy(Penal::getIdPartido));
		List<PenalesResponse> respuesta = new ArrayList<>();
		for (Partido p : partidos) {
			List<Penal> tiros = porPartido.get(p.getIdPartido());
			if (tiros != null && !tiros.isEmpty()) {
				respuesta.add(armar(p, tiros));
			}
		}
		return respuesta;
	}

	/** Registra el siguiente tiro (gol o fallo); el equipo que patea lo decide la tanda, alternando desde el local. */
	@Transactional
	public PenalesResponse registrar(Long idPartido, boolean convertido) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		validarTandaPosible(partido);
		List<Penal> tiros = penalRepository.findByIdPartidoOrderByOrdenAsc(idPartido);
		TandaPenales.Estado estado = TandaPenales.calcular(aBooleanos(tiros));
		if (estado.ganador() != null) {
			throw new ReglaNegocioException("La tanda de penales ya tiene ganador; no se pueden registrar mas tiros");
		}
		Equipos equipos = equiposDe(idPartido);
		Penal tiro = new Penal();
		tiro.setIdPartido(idPartido);
		tiro.setOrden(tiros.size() + 1);
		tiro.setIdEquipo("LOCAL".equals(estado.siguiente()) ? equipos.local() : equipos.visitante());
		tiro.setConvertido(convertido ? "S" : "N");
		tiros.add(penalRepository.save(tiro));
		return armar(partido, tiros);
	}

	/** Borra el ultimo tiro registrado (para corregir un error). */
	@Transactional
	public PenalesResponse deshacerUltimo(Long idPartido) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		validarTandaPosible(partido);
		List<Penal> tiros = penalRepository.findByIdPartidoOrderByOrdenAsc(idPartido);
		if (tiros.isEmpty()) {
			throw new ReglaNegocioException("No hay tiros para deshacer");
		}
		penalRepository.delete(tiros.remove(tiros.size() - 1));
		return armar(partido, tiros);
	}

	/** Solo en partidos de eliminatoria EN_JUEGO que van empatados. */
	private void validarTandaPosible(Partido partido) {
		if (!"EN_JUEGO".equals(partido.getEstado())) {
			throw new ReglaNegocioException(
					"Los penales solo se registran con el partido EN_JUEGO (estado actual: " + partido.getEstado() + ")");
		}
		if (!esEliminatoria(partido)) {
			throw new ReglaNegocioException("Los penales solo aplican a partidos de eliminatoria, no a la fase de grupos");
		}
		if (!empatado(partido)) {
			throw new ReglaNegocioException("Los penales solo se patean si el partido va empatado");
		}
	}

	static boolean esEliminatoria(Partido partido) {
		return !"Grupos".equalsIgnoreCase(partido.getFase());
	}

	static boolean empatado(Partido partido) {
		return partido.getGolesLocal().equals(partido.getGolesVisitante());
	}

	private record Equipos(Long local, Long visitante) {
	}

	private Equipos equiposDe(Long idPartido) {
		Long local = null;
		Long visitante = null;
		for (ParEqu pe : parEquRepository.buscarPorPartido(idPartido)) {
			if ("LOCAL".equals(pe.getRol())) {
				local = pe.getId().getIdEquipo();
			} else {
				visitante = pe.getId().getIdEquipo();
			}
		}
		if (local == null || visitante == null) {
			throw new ReglaNegocioException("El partido no tiene sus dos equipos asignados");
		}
		return new Equipos(local, visitante);
	}

	private static List<Boolean> aBooleanos(List<Penal> tiros) {
		return tiros.stream().map(t -> "S".equals(t.getConvertido())).toList();
	}

	private PenalesResponse armar(Partido partido, List<Penal> tiros) {
		Equipos equipos = equiposDe(partido.getIdPartido());
		TandaPenales.Estado estado = TandaPenales.calcular(aBooleanos(tiros));
		List<TiroPenalResponse> lista = new ArrayList<>();
		for (Penal t : tiros) {
			lista.add(new TiroPenalResponse(t.getOrden(), t.getIdEquipo(),
					t.getIdEquipo().equals(equipos.local()) ? "LOCAL" : "VISITANTE", "S".equals(t.getConvertido())));
		}
		Long idGanador = estado.ganador() == null ? null
				: "LOCAL".equals(estado.ganador()) ? equipos.local() : equipos.visitante();
		boolean aplica = esEliminatoria(partido) && empatado(partido);
		return new PenalesResponse(partido.getIdPartido(), equipos.local(), equipos.visitante(), aplica, lista,
				estado.golesLocal(), estado.golesVisitante(), estado.siguiente(), estado.ganador(), idGanador);
	}

}
