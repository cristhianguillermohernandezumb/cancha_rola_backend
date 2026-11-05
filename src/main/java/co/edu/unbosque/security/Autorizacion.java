package co.edu.unbosque.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.exception.AccesoDenegadoException;

/** Reglas de "quien puede tocar que" que no se pueden expresar solo con la URL. */
@Component
public class Autorizacion {

	/** Usuario que hizo la peticion, o vacio si no hay sesion. */
	public Optional<UsuarioAutenticado> actual() {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		if (autenticacion != null && autenticacion.getPrincipal() instanceof UsuarioAutenticado usuario) {
			return Optional.of(usuario);
		}
		return Optional.empty();
	}

	public boolean esAdmin() {
		return actual().map(u -> "ADMIN".equals(u.rol())).orElse(false);
	}

	/** Solo el propio usuario o un ADMIN. */
	public void exigirUsuarioPropio(Long idUsuario) {
		UsuarioAutenticado usuario = actual()
				.orElseThrow(() -> new AccesoDenegadoException("Debe iniciar sesion"));
		if (!"ADMIN".equals(usuario.rol()) && !usuario.idUsuario().equals(idUsuario)) {
			throw new AccesoDenegadoException("Solo puede acceder a sus propios datos");
		}
	}

	/** Solo el organizador que creo el torneo o un ADMIN. */
	public void exigirDuenoDeTorneo(Torneo torneo) {
		UsuarioAutenticado usuario = actual()
				.orElseThrow(() -> new AccesoDenegadoException("Debe iniciar sesion"));
		if ("ADMIN".equals(usuario.rol())) {
			return;
		}
		if (!usuario.idUsuario().equals(torneo.getOrganizador().getIdUsuario())) {
			throw new AccesoDenegadoException("Solo el organizador de este torneo puede modificarlo");
		}
	}

}
