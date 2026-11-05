package co.edu.unbosque.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import co.edu.unbosque.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Se ejecuta en cada peticion: si trae "Authorization: Bearer <token>" valido y el usuario sigue ACTIVO, deja
 * al usuario identificado. El rol se lee de la base (no del token) para que un cambio de rol o una
 * desactivacion apliquen de inmediato. Sin token valido no hace nada: las reglas de SecurityConfig deciden.
 */
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String PREFIJO = "Bearer ";

	private final JwtService jwtService;
	private final UsuarioRepository usuarioRepository;

	public JwtAuthFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
		this.jwtService = jwtService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String encabezado = request.getHeader("Authorization");
		if (encabezado != null && encabezado.startsWith(PREFIJO)) {
			jwtService.validar(encabezado.substring(PREFIJO.length()).trim()).flatMap(usuarioRepository::findById)
					.filter(u -> "ACTIVO".equals(u.getEstado())).ifPresent(u -> {
						UsuarioAutenticado principal = new UsuarioAutenticado(u.getIdUsuario(), u.getCorreo(),
								u.getRol());
						UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
								principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol())));
						SecurityContext contexto = SecurityContextHolder.createEmptyContext();
						contexto.setAuthentication(autenticacion);
						SecurityContextHolder.setContext(contexto);
					});
		}
		chain.doFilter(request, response);
	}

}
