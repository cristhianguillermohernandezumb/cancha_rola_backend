package co.edu.unbosque.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.CruceRequest;
import co.edu.unbosque.dto.CruceResponse;
import co.edu.unbosque.entity.Cruce;
import co.edu.unbosque.entity.EquTorId;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.repository.CruceRepository;
import co.edu.unbosque.repository.EquTorRepository;
import co.edu.unbosque.repository.PartidoRepository;
import co.edu.unbosque.security.Autorizacion;

/**
 * Cruces de las rondas eliminatorias. Primero se definen los cruces (aqui) y despues el organizador crea
 * los partidos de cada uno con la misma fase (ver FASE_POR_RONDA) y los mismos equipos.
 */
@Service
public class CruceService {

	private static final String FASE_GRUPOS = "Grupos";

	private static final Map<String, String> FASE_POR_RONDA = Map.of("32AVOS", "32avos", "OCTAVOS", "octavos");
	private static final Map<String, Integer> CRUCES_POR_RONDA = Map.of("32AVOS", 16, "OCTAVOS", 8);

	private final Autorizacion autorizacion;
	private final CruceRepository cruceRepository;
	private final EquTorRepository equTorRepository;
	private final PartidoRepository partidoRepository;
	private final TorneoService torneoService;
	private final EquipoService equipoService;

	public CruceService(CruceRepository cruceRepository, EquTorRepository equTorRepository,
			PartidoRepository partidoRepository, TorneoService torneoService, EquipoService equipoService,
			Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.cruceRepository = cruceRepository;
		this.equTorRepository = equTorRepository;
		this.partidoRepository = partidoRepository;
		this.torneoService = torneoService;
		this.equipoService = equipoService;
	}

	@Transactional(readOnly = true)
	public List<CruceResponse> listar(Long idTorneo, String ronda) {
		torneoService.buscarEntidad(idTorneo);
		List<Cruce> cruces = (ronda == null || ronda.isBlank()) ? cruceRepository.findByIdTorneoOrderByRondaAscOrdenAsc(idTorneo)
				: cruceRepository.findByIdTorneoAndRondaOrderByOrdenAsc(idTorneo, normalizar(ronda));
		return cruces.stream().map(this::aRespuesta).toList();
	}

	@Transactional
	public List<CruceResponse> reemplazar(Long idTorneo, String ronda, List<CruceRequest> pedidos) {
		String codigo = normalizar(ronda);
		Torneo torneo = torneoService.buscarEntidad(idTorneo);
		autorizacion.exigirDuenoDeTorneo(torneo);
		validarPuedeCambiar(torneo, codigo);
		int esperados = CRUCES_POR_RONDA.get(codigo);
		if (pedidos == null || pedidos.size() != esperados) {
			throw new ReglaNegocioException("La ronda " + codigo + " necesita exactamente " + esperados + " cruces");
		}
		Set<Long> usados = new HashSet<>();
		for (int i = 0; i < pedidos.size(); i++) {
			CruceRequest p = pedidos.get(i);
			if (p == null || p.idLocal() == null || p.idVisitante() == null) {
				throw new ReglaNegocioException("El cruce " + (i + 1) + " debe tener equipo local y visitante");
			}
			if (p.idLocal().equals(p.idVisitante())) {
				throw new ReglaNegocioException("El cruce " + (i + 1) + " tiene el mismo equipo de local y visitante");
			}
			for (Long idEquipo : List.of(p.idLocal(), p.idVisitante())) {
				if (!equTorRepository.existsById(new EquTorId(idEquipo, idTorneo))) {
					throw new ReglaNegocioException("El equipo " + idEquipo + " no esta inscrito en el torneo " + idTorneo);
				}
				if (!usados.add(idEquipo)) {
					throw new ReglaNegocioException("El equipo " + idEquipo + " aparece en mas de un cruce");
				}
			}
		}
		cruceRepository.deleteAll(cruceRepository.findByIdTorneoAndRondaOrderByOrdenAsc(idTorneo, codigo));
		cruceRepository.flush();
		List<Cruce> nuevos = new java.util.ArrayList<>();
		for (int i = 0; i < pedidos.size(); i++) {
			Cruce c = new Cruce();
			c.setIdTorneo(idTorneo);
			c.setRonda(codigo);
			c.setOrden(i + 1);
			c.setIdLocal(pedidos.get(i).idLocal());
			c.setIdVisitante(pedidos.get(i).idVisitante());
			nuevos.add(cruceRepository.save(c));
		}
		return nuevos.stream().map(this::aRespuesta).toList();
	}

	@Transactional
	public void eliminar(Long idTorneo, String ronda) {
		String codigo = normalizar(ronda);
		Torneo torneo = torneoService.buscarEntidad(idTorneo);
		autorizacion.exigirDuenoDeTorneo(torneo);
		validarPuedeCambiar(torneo, codigo);
		cruceRepository.deleteAll(cruceRepository.findByIdTorneoAndRondaOrderByOrdenAsc(idTorneo, codigo));
	}

	private String normalizar(String ronda) {
		String codigo = ronda == null ? "" : ronda.trim().toUpperCase();
		if (!FASE_POR_RONDA.containsKey(codigo)) {
			throw new ReglaNegocioException("Ronda invalida. Valores permitidos: " + String.join(", ", FASE_POR_RONDA.keySet()));
		}
		return codigo;
	}

	private void validarPuedeCambiar(Torneo torneo, String codigo) {
		String primera = Integer.valueOf(24).equals(torneo.getMaxEquipos()) ? "OCTAVOS" : "32AVOS";
		if (!primera.equals(codigo)) {
			throw new ReglaNegocioException("En este torneo (" + torneo.getMaxEquipos() + " equipos) la eliminatoria empieza en "
					+ primera + ", no en " + codigo);
		}
		if ("FINALIZADO".equals(torneo.getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo; no se pueden modificar los cruces");
		}
		List<Partido> partidos = partidoRepository.findByTorneo_IdTorneo(torneo.getIdTorneo());
		List<Partido> grupos = partidos.stream().filter(p -> FASE_GRUPOS.equalsIgnoreCase(p.getFase())).toList();
		if (grupos.isEmpty() || grupos.stream().anyMatch(p -> !"FINALIZADO".equals(p.getEstado()))) {
			throw new ReglaNegocioException("Los cruces se definen cuando terminan todos los partidos de la fase de grupos");
		}
		String fase = FASE_POR_RONDA.get(codigo);
		if (partidos.stream().anyMatch(p -> fase.equalsIgnoreCase(p.getFase()))) {
			throw new ReglaNegocioException("Ya hay partidos creados en la ronda " + codigo + "; eliminalos antes de cambiar los cruces");
		}
	}

	private CruceResponse aRespuesta(Cruce c) {
		return new CruceResponse(c.getIdCruce(), c.getIdTorneo(), c.getRonda(), c.getOrden(), c.getIdLocal(),
				equipoService.buscarEntidad(c.getIdLocal()).getNombre(), c.getIdVisitante(),
				equipoService.buscarEntidad(c.getIdVisitante()).getNombre());
	}

}
