package co.edu.unbosque.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.security.UsuarioAutenticado;
import co.edu.unbosque.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Registra en la tabla auditoria cada POST, PUT, PATCH y DELETE que termina bien (codigo 2xx). Asi no hay que
 * tocar cada servicio. El usuario sale del token JWT. Las rutas publicas (sin sesion) no se auditan;
 * el LOGIN lo registra AuthService.
 */
@Component
public class AuditoriaInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(AuditoriaInterceptor.class);

	/** Segmentos de la URL que son acciones y no entidades. */
	private static final Set<String> CAMBIOS_DE_ESTADO = Set.of("estado", "activar", "desactivar");
	private static final Set<String> ACCIONES = Set.of("estado", "activar", "desactivar", "pagar", "notificacion",
			"camiseta", "retirar", "documento");

	/** Nombre de la entidad segun el ultimo segmento de la URL. */
	private static final Map<String, String> ENTIDADES = Map.ofEntries(Map.entry("usuarios", "USUARIO"),
			Map.entry("torneos", "TORNEO"), Map.entry("equipos", "EQUIPO"), Map.entry("grupos", "GRUPO"),
			Map.entry("jugadores", "JUGADOR"), Map.entry("inscripciones-jugador", "INSCRIPCION_JUGADOR"),
			Map.entry("canchas", "CANCHA"), Map.entry("arbitros", "ARBITRO"), Map.entry("entrenadores", "ENTRENADOR"),
			Map.entry("partidos", "PARTIDO"), Map.entry("alineacion", "ALINEACION"),
			Map.entry("eventos", "EVENTO_PARTIDO"), Map.entry("seguimientos", "TOR_USU"));

	private final AuditoriaService auditoriaService;
	private final Autorizacion autorizacion;

	public AuditoriaInterceptor(AuditoriaService auditoriaService, Autorizacion autorizacion) {
		this.auditoriaService = auditoriaService;
		this.autorizacion = autorizacion;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		try {
			String metodo = request.getMethod();
			if (ex != null || response.getStatus() >= 400 || "GET".equals(metodo) || "OPTIONS".equals(metodo)
					|| "HEAD".equals(metodo)) {
				return;
			}
			Long idUsuario = autorizacion.actual().map(UsuarioAutenticado::idUsuario).orElse(null);
			if (idUsuario == null) {
				return;
			}
			String ruta = request.getRequestURI().substring(request.getContextPath().length());
			List<String> segmentos = new ArrayList<>(List.of(ruta.split("/")));
			segmentos.removeIf(String::isBlank);
			if (segmentos.isEmpty()) {
				return;
			}
			List<String> ids = segmentos.stream().filter(this::esNumero).toList();
			boolean guardado = auditoriaService.registrar(idUsuario, entidad(segmentos),
					ids.isEmpty() ? "NUEVO" : String.join("-", ids), accion(metodo, segmentos),
					metodo + " " + ruta, torneo(segmentos));
			if (!guardado) {
				log.warn("Auditoria omitida: no existe el usuario {}", idUsuario);
			}
		} catch (Exception e) {
			// La auditoria nunca debe romper la respuesta de la operacion ya realizada.
			log.warn("No se pudo registrar la auditoria: {}", e.getMessage());
		}
	}

	private String accion(String metodo, List<String> segmentos) {
		if ("DELETE".equals(metodo)) {
			return "ELIMINAR";
		}
		if ("POST".equals(metodo)) {
			return "CREAR";
		}
		return CAMBIOS_DE_ESTADO.contains(segmentos.get(segmentos.size() - 1)) ? "CAMBIAR_ESTADO" : "MODIFICAR";
	}

	private String entidad(List<String> segmentos) {
		List<String> nombres = new ArrayList<>(segmentos);
		nombres.removeIf(s -> esNumero(s) || ACCIONES.contains(s));
		if (nombres.isEmpty()) {
			return "DESCONOCIDA";
		}
		String ultimo = nombres.get(nombres.size() - 1);
		if (nombres.size() > 1 && ultimo.equals("equipos")) {
			return switch (nombres.get(0)) {
				case "partidos" -> "PAR_EQU";
				case "torneos" -> "EQU_TOR";
				case "grupos" -> "EQU_GRU";
				default -> "EQUIPO";
			};
		}
		if (nombres.size() > 1 && ultimo.equals("arbitros") && nombres.get(0).equals("partidos")) {
			return "PAR_ARB";
		}
		return ENTIDADES.getOrDefault(ultimo, ultimo.toUpperCase());
	}

	/** El torneo se conoce cuando la URL empieza por /torneos/{id}. */
	private Long torneo(List<String> segmentos) {
		if (segmentos.size() > 1 && segmentos.get(0).equals("torneos") && esNumero(segmentos.get(1))) {
			return Long.valueOf(segmentos.get(1));
		}
		return null;
	}

	private boolean esNumero(String texto) {
		return !texto.isEmpty() && texto.length() < 18 && texto.chars().allMatch(Character::isDigit);
	}

}
