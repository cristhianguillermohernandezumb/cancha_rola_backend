package co.edu.unbosque.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.GoleadorResponse;
import co.edu.unbosque.dto.PosicionResponse;
import co.edu.unbosque.entity.EquTor;
import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.repository.EquGruRepository;
import co.edu.unbosque.repository.EquTorRepository;
import co.edu.unbosque.repository.EventoPartidoRepository;
import co.edu.unbosque.repository.ParEquRepository;

/** Estadisticas de un torneo calculadas con los partidos FINALIZADOS y los eventos registrados. */
@Service
public class EstadisticaService {

	private static final int PUNTOS_VICTORIA = 3;
	private static final int PUNTOS_EMPATE = 1;

	private final EquTorRepository equTorRepository;
	private final EquGruRepository equGruRepository;
	private final ParEquRepository parEquRepository;
	private final EventoPartidoRepository eventoRepository;
	private final TorneoService torneoService;
	private final GrupoService grupoService;
	private final JugadorService jugadorService;
	private final EquipoService equipoService;

	public EstadisticaService(EquTorRepository equTorRepository, EquGruRepository equGruRepository,
			ParEquRepository parEquRepository, EventoPartidoRepository eventoRepository,
			TorneoService torneoService, GrupoService grupoService, JugadorService jugadorService,
			EquipoService equipoService) {
		this.equTorRepository = equTorRepository;
		this.equGruRepository = equGruRepository;
		this.parEquRepository = parEquRepository;
		this.eventoRepository = eventoRepository;
		this.torneoService = torneoService;
		this.grupoService = grupoService;
		this.jugadorService = jugadorService;
		this.equipoService = equipoService;
	}

	@Transactional(readOnly = true)
	public List<PosicionResponse> tablaDePosiciones(Long idTorneo, Long idGrupo) {
		torneoService.buscarEntidad(idTorneo);
		Set<Long> idsGrupo = null;
		if (idGrupo != null) {
			Grupo grupo = grupoService.buscarEntidad(idGrupo);
			if (!grupo.getTorneo().getIdTorneo().equals(idTorneo)) {
				throw new ReglaNegocioException("El grupo " + idGrupo + " no pertenece al torneo " + idTorneo);
			}
			idsGrupo = equGruRepository.buscarPorGrupo(idGrupo).stream().map(e -> e.getId().getIdEquipo())
					.collect(Collectors.toSet());
		}
		Map<Long, String> equipos = new LinkedHashMap<>();
		for (EquTor et : equTorRepository.buscarPorTorneo(idTorneo)) {
			Long idEquipo = et.getId().getIdEquipo();
			if (idsGrupo == null || idsGrupo.contains(idEquipo)) {
				equipos.put(idEquipo, et.getEquipo().getNombre());
			}
		}
		return calcularTabla(equipos, resultados(idTorneo));
	}

	@Transactional(readOnly = true)
	public List<GoleadorResponse> goleadores(Long idTorneo, int limite) {
		return goleadores(idTorneo, limite, "todas");
	}

	@Transactional(readOnly = true)
	public List<GoleadorResponse> goleadores(Long idTorneo, int limite, String fase) {
		torneoService.buscarEntidad(idTorneo);
		if (limite < 1 || limite > 100) {
			throw new ReglaNegocioException("limite debe estar entre 1 y 100");
		}
		String f = fase == null || fase.isBlank() ? "todas" : fase.toLowerCase();
		List<Object[]> filas = switch (f) {
		case "todas" -> eventoRepository.contarGolesPorJugador(idTorneo);
		case "grupos" -> eventoRepository.contarGolesPorJugadorEnGrupos(idTorneo);
		case "eliminatoria" -> eventoRepository.contarGolesPorJugadorEnEliminatoria(idTorneo);
		default -> throw new ReglaNegocioException("fase debe ser grupos, eliminatoria o todas");
		};
		List<GoleadorResponse> todos = new ArrayList<>();
		for (Object[] fila : filas) {
			Long idJugador = (Long) fila[0];
			Long idEquipo = (Long) fila[1];
			Long goles = (Long) fila[2];
			Jugador jugador = jugadorService.buscarEntidad(idJugador);
			String equipo = equipoService.buscarEntidad(idEquipo).getNombre();
			todos.add(new GoleadorResponse(0, idJugador, jugador.getPrimerNombre() + " " + jugador.getPrimerApellido(),
					idEquipo, equipo, goles));
		}
		todos.sort(Comparator.comparing(GoleadorResponse::goles).reversed()
				.thenComparing(GoleadorResponse::nombreJugador));
		List<GoleadorResponse> resultado = new ArrayList<>();
		for (int i = 0; i < Math.min(limite, todos.size()); i++) {
			GoleadorResponse g = todos.get(i);
			resultado.add(new GoleadorResponse(i + 1, g.idJugador(), g.nombreJugador(), g.idEquipo(),
					g.nombreEquipo(), g.goles()));
		}
		return resultado;
	}

	/** Un partido terminado: quien fue local, quien visitante y el marcador. */
	record Resultado(Long idLocal, Long idVisitante, int golesLocal, int golesVisitante) {
	}

	private List<Resultado> resultados(Long idTorneo) {
		Map<Long, List<ParEqu>> porPartido = parEquRepository.buscarDePartidosFinalizados(idTorneo).stream()
				.collect(Collectors.groupingBy(p -> p.getId().getIdPartido()));
		List<Resultado> lista = new ArrayList<>();
		for (List<ParEqu> par : porPartido.values()) {
			ParEqu local = par.stream().filter(p -> "LOCAL".equals(p.getRol())).findFirst().orElse(null);
			ParEqu visitante = par.stream().filter(p -> "VISITANTE".equals(p.getRol())).findFirst().orElse(null);
			if (local == null || visitante == null) {
				continue;
			}
			lista.add(new Resultado(local.getId().getIdEquipo(), visitante.getId().getIdEquipo(),
					local.getPartido().getGolesLocal(), local.getPartido().getGolesVisitante()));
		}
		return lista;
	}

	static List<PosicionResponse> calcularTabla(Map<Long, String> equipos, List<Resultado> resultados) {
		Map<Long, int[]> datos = new HashMap<>();
		equipos.keySet().forEach(id -> datos.put(id, new int[6]));
		for (Resultado r : resultados) {
			int[] l = datos.get(r.idLocal());
			int[] v = datos.get(r.idVisitante());
			if (l == null || v == null) {
				continue;
			}
			l[0]++;
			v[0]++;
			l[4] += r.golesLocal();
			l[5] += r.golesVisitante();
			v[4] += r.golesVisitante();
			v[5] += r.golesLocal();
			if (r.golesLocal() > r.golesVisitante()) {
				l[1]++;
				v[3]++;
			} else if (r.golesLocal() < r.golesVisitante()) {
				v[1]++;
				l[3]++;
			} else {
				l[2]++;
				v[2]++;
			}
		}
		List<PosicionResponse> filas = new ArrayList<>();
		for (Map.Entry<Long, String> e : equipos.entrySet()) {
			int[] d = datos.get(e.getKey());
			filas.add(new PosicionResponse(0, e.getKey(), e.getValue(), d[0], d[1], d[2], d[3], d[4], d[5],
					d[4] - d[5], d[1] * PUNTOS_VICTORIA + d[2] * PUNTOS_EMPATE));
		}
		filas.sort(Comparator.comparing(PosicionResponse::puntos).reversed()
				.thenComparing(Comparator.comparing(PosicionResponse::diferenciaGoles).reversed())
				.thenComparing(Comparator.comparing(PosicionResponse::golesFavor).reversed())
				.thenComparing(PosicionResponse::nombreEquipo));
		List<PosicionResponse> numeradas = new ArrayList<>();
		for (int i = 0; i < filas.size(); i++) {
			PosicionResponse f = filas.get(i);
			numeradas.add(new PosicionResponse(i + 1, f.idEquipo(), f.nombreEquipo(), f.partidosJugados(),
					f.ganados(), f.empatados(), f.perdidos(), f.golesFavor(), f.golesContra(),
					f.diferenciaGoles(), f.puntos()));
		}
		return numeradas;
	}

}
