package co.edu.unbosque.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.AlineacionResponse;
import co.edu.unbosque.dto.CopiaAlineacionResponse;
import co.edu.unbosque.entity.Alineacion;
import co.edu.unbosque.entity.AlineacionId;
import co.edu.unbosque.entity.InscripcionJugador;
import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.entity.ParEquId;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.AlineacionMapper;
import co.edu.unbosque.repository.AlineacionRepository;
import co.edu.unbosque.repository.InscripcionJugadorRepository;
import co.edu.unbosque.repository.ParEquRepository;

/** Jugadores que participan (titulares y suplentes) de cada equipo en un partido. */
@Service
public class AlineacionService {

	private final Autorizacion autorizacion;

	/** Titulares permitidos segun la modalidad del torneo. */
	private static final Map<String, Integer> TITULARES = Map.of("FUTBOL_5", 5, "FUTBOL_7", 7, "FUTBOL_8", 8,
			"FUTBOL_11", 11);

	private final AlineacionRepository alineacionRepository;
	private final ParEquRepository parEquRepository;
	private final PartidoService partidoService;
	private final InscripcionJugadorService inscripcionJugadorService;
	private final AlineacionMapper alineacionMapper;
	private final InscripcionJugadorRepository inscripcionRepository;

	public AlineacionService(AlineacionRepository alineacionRepository, ParEquRepository parEquRepository,
			PartidoService partidoService, InscripcionJugadorService inscripcionJugadorService,
			AlineacionMapper alineacionMapper, Autorizacion autorizacion,
			InscripcionJugadorRepository inscripcionRepository) {
		this.inscripcionRepository = inscripcionRepository;
		this.autorizacion = autorizacion;
		this.alineacionRepository = alineacionRepository;
		this.parEquRepository = parEquRepository;
		this.partidoService = partidoService;
		this.inscripcionJugadorService = inscripcionJugadorService;
		this.alineacionMapper = alineacionMapper;
	}

	@Transactional
	public AlineacionResponse agregar(Long idPartido, Long idEquipo, Long idJugador, String titular) {
		String t = normalizarTitular(titular);
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if ("FINALIZADO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("El partido ya finalizo; no se puede modificar la alineacion");
		}
		ParEqu parEqu = buscarParEqu(partido, idEquipo);
		Long idTorneo = partido.getTorneo().getIdTorneo();
		InscripcionJugador inscripcion = inscripcionJugadorService.buscarEntidad(idTorneo, idEquipo, idJugador);
		if (alineacionRepository.existeJugadorEnPartido(idPartido, idJugador)) {
			throw new ReglaNegocioException("El jugador ya esta en la alineacion de este partido");
		}
		if ("S".equals(t)) {
			validarCupoTitulares(partido, idEquipo);
		}
		Alineacion alineacion = new Alineacion();
		alineacion.setId(new AlineacionId(idPartido, idEquipo, idTorneo, idJugador));
		alineacion.setTitular(t);
		alineacion.setMinutosJugados(0);
		Alineacion guardada = alineacionRepository.save(alineacion);
		// Las relaciones son de solo lectura: se asignan para que la respuesta lleve el nombre.
		guardada.setParEqu(parEqu);
		guardada.setInscripcionJugador(inscripcion);
		return alineacionMapper.toResponse(guardada);
	}

	/**
	 * Copia la ultima alineacion que el equipo uso (su partido anterior mas reciente con alineacion) a este partido,
	 * para armarla de un golpe. Los titulares se copian primero y respetan el cupo de la modalidad; quien no este
	 * inscrito en este torneo o ya este en el partido se omite.
	 */
	@Transactional
	public CopiaAlineacionResponse copiarAnterior(Long idPartido, Long idEquipo) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if (!"PROGRAMADO".equals(partido.getEstado()) && !"APLAZADO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se copia la alineacion antes de que empiece el partido");
		}
		ParEqu parEqu = buscarParEqu(partido, idEquipo);
		List<Long> origen = alineacionRepository.ultimosPartidosConAlineacion(idEquipo, idPartido,
				partido.getFechaHora(), PageRequest.of(0, 1));
		if (origen.isEmpty()) {
			throw new ReglaNegocioException("El equipo no tiene una alineacion anterior para copiar");
		}
		Long idTorneo = partido.getTorneo().getIdTorneo();
		Map<Long, InscripcionJugador> inscritos = inscripcionRepository.buscarPorEquipoYTorneo(idEquipo, idTorneo)
				.stream().collect(Collectors.toMap(i -> i.getId().getIdJugador(), Function.identity()));
		int maximo = TITULARES.getOrDefault(partido.getTorneo().getModalidad(), 11);
		long titulares = alineacionRepository.contarTitulares(idPartido, idEquipo);
		List<Alineacion> previa = new ArrayList<>(alineacionRepository.buscarPorPartidoYEquipo(origen.get(0), idEquipo));
		previa.sort(Comparator.comparing((Alineacion a) -> !"S".equals(a.getTitular())));
		List<AlineacionResponse> agregados = new ArrayList<>();
		int omitidos = 0;
		for (Alineacion anterior : previa) {
			Long idJugador = anterior.getId().getIdJugador();
			InscripcionJugador inscripcion = inscritos.get(idJugador);
			if (inscripcion == null || alineacionRepository.existeJugadorEnPartido(idPartido, idJugador)) {
				omitidos++;
				continue;
			}
			boolean titular = "S".equals(anterior.getTitular()) && titulares < maximo;
			if (titular) {
				titulares++;
			}
			Alineacion nueva = new Alineacion();
			nueva.setId(new AlineacionId(idPartido, idEquipo, idTorneo, idJugador));
			nueva.setTitular(titular ? "S" : "N");
			nueva.setMinutosJugados(0);
			Alineacion guardada = alineacionRepository.save(nueva);
			guardada.setParEqu(parEqu);
			guardada.setInscripcionJugador(inscripcion);
			agregados.add(alineacionMapper.toResponse(guardada));
		}
		return new CopiaAlineacionResponse(origen.get(0), agregados, omitidos);
	}

	@Transactional(readOnly = true)
	public List<AlineacionResponse> listarPorPartido(Long idPartido) {
		partidoService.buscarEntidad(idPartido);
		return alineacionRepository.buscarPorPartido(idPartido).stream().map(alineacionMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<AlineacionResponse> listarPorPartidoYEquipo(Long idPartido, Long idEquipo) {
		buscarParEqu(partidoService.buscarEntidad(idPartido), idEquipo);
		return alineacionRepository.buscarPorPartidoYEquipo(idPartido, idEquipo).stream()
				.map(alineacionMapper::toResponse).toList();
	}

	/**
	 * Cambia titular y/o minutos (los dos son opcionales). El titular solo se cambia antes de empezar; los minutos
	 * solo existen cuando el partido esta o estuvo EN_JUEGO.
	 */
	@Transactional
	public AlineacionResponse actualizar(Long idPartido, Long idEquipo, Long idJugador, String titular,
			Integer minutos) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		Alineacion alineacion = buscarEntidad(partido, idEquipo, idJugador);
		boolean sinEmpezar = "PROGRAMADO".equals(partido.getEstado()) || "APLAZADO".equals(partido.getEstado());
		if (titular != null && !titular.isBlank()) {
			String t = normalizarTitular(titular);
			if (!t.equals(alineacion.getTitular())) {
				if (!sinEmpezar) {
					throw new ReglaNegocioException("Solo se cambia titular/suplente antes de que empiece el partido");
				}
				if ("S".equals(t)) {
					validarCupoTitulares(partido, idEquipo);
				}
				alineacion.setTitular(t);
			}
		}
		if (minutos != null) {
			if (minutos < 0 || minutos > 120) {
				throw new ReglaNegocioException("Los minutos jugados deben estar entre 0 y 120");
			}
			if (sinEmpezar && minutos > 0) {
				throw new ReglaNegocioException("No hay minutos jugados en un partido que no ha empezado");
			}
			alineacion.setMinutosJugados(minutos);
		}
		return alineacionMapper.toResponse(alineacionRepository.save(alineacion));
	}

	@Transactional
	public void quitar(Long idPartido, Long idEquipo, Long idJugador) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		Alineacion alineacion = buscarEntidad(partido, idEquipo, idJugador);
		if (!"PROGRAMADO".equals(partido.getEstado()) && !"APLAZADO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se quitan jugadores de partidos PROGRAMADO o APLAZADO");
		}
		alineacionRepository.deleteById(alineacion.getId());
	}

	/** Lo usan otros servicios (eventos del partido) para validar que el jugador esta en la alineacion. */
	@Transactional(readOnly = true)
	public Alineacion buscarEntidad(Partido partido, Long idEquipo, Long idJugador) {
		AlineacionId id = new AlineacionId(partido.getIdPartido(), idEquipo, partido.getTorneo().getIdTorneo(),
				idJugador);
		return alineacionRepository.findById(id).orElseThrow(() -> new NoEncontradoException("El jugador "
				+ idJugador + " no esta en la alineacion del equipo " + idEquipo + " en el partido "
				+ partido.getIdPartido()));
	}

	private ParEqu buscarParEqu(Partido partido, Long idEquipo) {
		return parEquRepository
				.findById(new ParEquId(partido.getIdPartido(), idEquipo, partido.getTorneo().getIdTorneo()))
				.orElseThrow(() -> new ReglaNegocioException(
						"El equipo " + idEquipo + " no juega el partido " + partido.getIdPartido()));
	}

	private void validarCupoTitulares(Partido partido, Long idEquipo) {
		int maximo = TITULARES.getOrDefault(partido.getTorneo().getModalidad(), 11);
		if (alineacionRepository.contarTitulares(partido.getIdPartido(), idEquipo) >= maximo) {
			throw new ReglaNegocioException("El equipo ya tiene los " + maximo + " titulares de "
					+ partido.getTorneo().getModalidad());
		}
	}

	private String normalizarTitular(String titular) {
		String t = titular == null ? "" : titular.toUpperCase();
		if (!t.equals("S") && !t.equals("N")) {
			throw new ReglaNegocioException("titular debe ser S (titular) o N (suplente)");
		}
		return t;
	}

}
