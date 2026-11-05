package co.edu.unbosque.service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.PosicionResponse;
import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.entity.TorUsu;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.repository.ParEquRepository;
import co.edu.unbosque.repository.PartidoRepository;
import co.edu.unbosque.repository.PenalRepository;
import co.edu.unbosque.repository.TorUsuRepository;

/**
 * Arma y manda los correos de un torneo a los usuarios que lo siguen con las notificaciones activas (S):
 * uno cuando el torneo inicia y otro cuando termina, con el campeon.
 */
@Service
public class NotificacionTorneoService {

	private static final Logger log = LoggerFactory.getLogger(NotificacionTorneoService.class);
	private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final TorneoService torneoService;
	private final TorUsuRepository torUsuRepository;
	private final PartidoRepository partidoRepository;
	private final ParEquRepository parEquRepository;
	private final PenalRepository penalRepository;
	private final EstadisticaService estadisticaService;
	private final CorreoService correoService;

	public NotificacionTorneoService(TorneoService torneoService, TorUsuRepository torUsuRepository,
			PartidoRepository partidoRepository, ParEquRepository parEquRepository, PenalRepository penalRepository,
			EstadisticaService estadisticaService, CorreoService correoService) {
		this.torneoService = torneoService;
		this.torUsuRepository = torUsuRepository;
		this.partidoRepository = partidoRepository;
		this.parEquRepository = parEquRepository;
		this.penalRepository = penalRepository;
		this.estadisticaService = estadisticaService;
		this.correoService = correoService;
	}

	@Transactional(readOnly = true)
	public void notificarInicio(Long idTorneo) {
		Torneo torneo = torneoService.buscarEntidad(idTorneo);
		List<Usuario> destinatarios = destinatarios(idTorneo);
		log.info("Torneo {} inicio: avisando a {} seguidores", torneo.getNombre(), destinatarios.size());
		for (Usuario u : destinatarios) {
			Map<String, Object> vars = base(torneo, u);
			correoService.enviar(u.getCorreo(), "Ya empezo " + torneo.getNombre(), "torneo-inicio", vars);
		}
	}

	@Transactional(readOnly = true)
	public void notificarFinal(Long idTorneo) {
		Torneo torneo = torneoService.buscarEntidad(idTorneo);
		List<Usuario> destinatarios = destinatarios(idTorneo);
		log.info("Torneo {} finalizo: avisando a {} seguidores", torneo.getNombre(), destinatarios.size());
		if (destinatarios.isEmpty()) {
			return;
		}
		Map<String, Object> resultado = resultadoFinal(torneo);
		for (Usuario u : destinatarios) {
			Map<String, Object> vars = base(torneo, u);
			vars.putAll(resultado);
			correoService.enviar(u.getCorreo(), "Termino " + torneo.getNombre() + ": conoce al campeon",
					"torneo-final", vars);
		}
	}

	/** Seguidores ACTIVOS que dejaron las notificaciones activadas. */
	private List<Usuario> destinatarios(Long idTorneo) {
		return torUsuRepository.buscarPorTorneo(idTorneo).stream().filter(t -> "S".equals(t.getNotificacion()))
				.map(TorUsu::getUsuario).filter(u -> u != null && "ACTIVO".equals(u.getEstado())).toList();
	}

	private Map<String, Object> base(Torneo torneo, Usuario usuario) {
		Map<String, Object> vars = new HashMap<>();
		vars.put("nombre", usuario.getPrimerNombre());
		vars.put("torneo", torneo.getNombre());
		vars.put("categoria", torneo.getCategoria());
		vars.put("modalidad", torneo.getModalidad());
		vars.put("fechaInicio", torneo.getFechaInicio().format(FECHA));
		vars.put("fechaFin", torneo.getFechaFin().format(FECHA));
		return vars;
	}

	/**
	 * Campeon = ganador del partido de la final (por goles o, si empataron, por penales). Si el torneo no tuvo
	 * final se usa el lider de la tabla; si tampoco hubo partidos, el correo sale sin campeon.
	 */
	private Map<String, Object> resultadoFinal(Torneo torneo) {
		Map<String, Object> vars = new HashMap<>();
		vars.put("campeon", null);
		vars.put("subcampeon", null);
		vars.put("marcador", null);
		vars.put("penales", null);
		vars.put("porFinal", false);

		Optional<Partido> final_ = partidoRepository.findByTorneo_IdTorneo(torneo.getIdTorneo()).stream()
				.filter(p -> "final".equalsIgnoreCase(p.getFase() == null ? "" : p.getFase().trim())
						&& "FINALIZADO".equals(p.getEstado()))
				.findFirst();
		if (final_.isPresent()) {
			Partido p = final_.get();
			String local = nombreEquipo(p, "LOCAL");
			String visitante = nombreEquipo(p, "VISITANTE");
			String ganador = ganadorDe(p);
			if (local != null && visitante != null && ganador != null) {
				boolean gana = "LOCAL".equals(ganador);
				vars.put("campeon", gana ? local : visitante);
				vars.put("subcampeon", gana ? visitante : local);
				vars.put("marcador", local + " " + p.getGolesLocal() + " - " + p.getGolesVisitante() + " " + visitante);
				if (p.getGolesLocal().equals(p.getGolesVisitante())) {
					vars.put("penales", "Se definio en la tanda de penales");
				}
				vars.put("porFinal", true);
				return vars;
			}
		}
		List<PosicionResponse> tabla = estadisticaService.tablaDePosiciones(torneo.getIdTorneo(), null);
		if (!tabla.isEmpty() && tabla.get(0).partidosJugados() > 0) {
			vars.put("campeon", tabla.get(0).nombreEquipo());
		}
		return vars;
	}

	private String ganadorDe(Partido p) {
		if (p.getGolesLocal() > p.getGolesVisitante()) {
			return "LOCAL";
		}
		if (p.getGolesLocal() < p.getGolesVisitante()) {
			return "VISITANTE";
		}
		List<Boolean> tiros = penalRepository.findByIdPartidoOrderByOrdenAsc(p.getIdPartido()).stream()
				.map(t -> "S".equals(t.getConvertido())).toList();
		return TandaPenales.calcular(tiros).ganador();
	}

	private String nombreEquipo(Partido p, String rol) {
		return parEquRepository.buscarPorPartido(p.getIdPartido()).stream().filter(pe -> rol.equals(pe.getRol()))
				.map(ParEqu::getEquTor).filter(et -> et != null && et.getEquipo() != null)
				.map(et -> et.getEquipo().getNombre()).findFirst().orElse(null);
	}

}
