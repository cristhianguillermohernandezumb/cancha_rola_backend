package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.UsuarioActualizarRequest;
import co.edu.unbosque.dto.UsuarioRequest;
import co.edu.unbosque.dto.UsuarioResponse;
import co.edu.unbosque.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioResponse registrar(@Valid @RequestBody UsuarioRequest request) {
		return usuarioService.registrar(request);
	}

	@GetMapping
	public List<UsuarioResponse> listar() {
		return usuarioService.listar();
	}

	@GetMapping("/{id}")
	public UsuarioResponse obtener(@PathVariable Long id) {
		return usuarioService.obtener(id);
	}

	@PutMapping("/{id}")
	public UsuarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioActualizarRequest request) {
		return usuarioService.actualizar(id, request);
	}

	@PatchMapping("/{id}/desactivar")
	public UsuarioResponse desactivar(@PathVariable Long id) {
		return usuarioService.desactivar(id);
	}

	/** Solo ADMIN. Ejemplo: PATCH /usuarios/5/rol?valor=ORGANIZADOR */
	@PatchMapping("/{id}/rol")
	public UsuarioResponse cambiarRol(@PathVariable Long id, @RequestParam String valor) {
		return usuarioService.cambiarRol(id, valor);
	}

	@PatchMapping("/{id}/activar")
	public UsuarioResponse activar(@PathVariable Long id) {
		return usuarioService.activar(id);
	}

}
