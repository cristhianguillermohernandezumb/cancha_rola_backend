package co.edu.unbosque.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.LoginRequest;
import co.edu.unbosque.dto.LoginResponse;
import co.edu.unbosque.dto.UsuarioResponse;
import co.edu.unbosque.exception.AccesoDenegadoException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.service.AuthService;
import co.edu.unbosque.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final UsuarioService usuarioService;
	private final Autorizacion autorizacion;

	public AuthController(AuthService authService, UsuarioService usuarioService, Autorizacion autorizacion) {
		this.authService = authService;
		this.usuarioService = usuarioService;
		this.autorizacion = autorizacion;
	}

	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		return authService.login(request);
	}

	@GetMapping("/me")
	public UsuarioResponse me() {
		Long id = autorizacion.actual().orElseThrow(() -> new AccesoDenegadoException("Debe iniciar sesion"))
				.idUsuario();
		return usuarioService.obtener(id);
	}

}
