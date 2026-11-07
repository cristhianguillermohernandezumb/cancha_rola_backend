package co.edu.unbosque.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.LoginRequest;
import co.edu.unbosque.dto.LoginResponse;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.exception.CredencialesInvalidasException;
import co.edu.unbosque.mapper.UsuarioMapper;
import co.edu.unbosque.repository.UsuarioRepository;
import co.edu.unbosque.security.JwtService;

@Service
public class AuthService {

	private static final String MENSAJE = "Correo o contrasena incorrectos, o usuario inactivo";

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuditoriaService auditoriaService;
	private final UsuarioMapper usuarioMapper;
	/** Hash de relleno para que el tiempo de respuesta no delate si el correo existe. */
	private final String hashRelleno;

	public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuditoriaService auditoriaService, UsuarioMapper usuarioMapper) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.auditoriaService = auditoriaService;
		this.usuarioMapper = usuarioMapper;
		this.hashRelleno = passwordEncoder.encode("relleno-no-es-una-clave-real");
	}

	@Transactional
	public LoginResponse login(LoginRequest request) {
		Usuario usuario = usuarioRepository.findByCorreo(request.correo()).orElse(null);
		String hash = usuario == null ? hashRelleno : usuario.getContrasenia();
		boolean claveCorrecta = passwordEncoder.matches(request.contrasenia(), hash);
		if (usuario == null || !claveCorrecta || !"ACTIVO".equals(usuario.getEstado())) {
			throw new CredencialesInvalidasException(MENSAJE);
		}
		auditoriaService.registrar(usuario.getIdUsuario(), "USUARIO", String.valueOf(usuario.getIdUsuario()),
				"LOGIN", "Inicio de sesion", null);
		return new LoginResponse(jwtService.generar(usuario), "Bearer", jwtService.getExpiracionMinutos(),
				usuarioMapper.toResponse(usuario));
	}

}
