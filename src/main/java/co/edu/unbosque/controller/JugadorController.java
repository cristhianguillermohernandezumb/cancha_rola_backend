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

import co.edu.unbosque.dto.JugadorRequest;
import co.edu.unbosque.dto.JugadorResponse;
import co.edu.unbosque.service.JugadorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

	private final JugadorService jugadorService;

	public JugadorController(JugadorService jugadorService) {
		this.jugadorService = jugadorService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public JugadorResponse crear(@Valid @RequestBody JugadorRequest request) {
		return jugadorService.crear(request);
	}

	/** Lista todos. Filtros opcionales: /jugadores?estado=LESIONADO o /jugadores?posicion=PORTERO */
	@GetMapping
	public List<JugadorResponse> listar(@RequestParam(required = false) String estado,
			@RequestParam(required = false) String posicion) {
		return jugadorService.listar(estado, posicion);
	}

	@GetMapping("/{id}")
	public JugadorResponse obtener(@PathVariable Long id) {
		return jugadorService.obtener(id);
	}

	/** Ejemplo: /jugadores/documento/CC/1001001001 */
	@GetMapping("/documento/{tipo}/{numero}")
	public JugadorResponse obtenerPorDocumento(@PathVariable String tipo, @PathVariable String numero) {
		return jugadorService.obtenerPorDocumento(tipo, numero);
	}

	@PutMapping("/{id}")
	public JugadorResponse actualizar(@PathVariable Long id, @Valid @RequestBody JugadorRequest request) {
		return jugadorService.actualizar(id, request);
	}

	/** Ejemplo: /jugadores/1/estado?valor=LESIONADO */
	@PatchMapping("/{id}/estado")
	public JugadorResponse cambiarEstado(@PathVariable Long id, @RequestParam String valor) {
		return jugadorService.cambiarEstado(id, valor);
	}

}
