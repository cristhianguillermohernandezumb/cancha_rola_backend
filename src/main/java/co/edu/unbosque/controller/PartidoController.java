package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import co.edu.unbosque.dto.PartidoRequest;
import co.edu.unbosque.dto.PartidoResponse;
import co.edu.unbosque.service.PartidoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/partidos")
public class PartidoController {

	private final PartidoService partidoService;

	public PartidoController(PartidoService partidoService) {
		this.partidoService = partidoService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PartidoResponse crear(@Valid @RequestBody PartidoRequest request) {
		return partidoService.crear(request);
	}

	@GetMapping
	public List<PartidoResponse> listar(@RequestParam(required = false) Long idTorneo,
			@RequestParam(required = false) Integer jornada, @RequestParam(required = false) String estado) {
		return partidoService.listar(idTorneo, jornada, estado);
	}

	@GetMapping("/{id}")
	public PartidoResponse obtener(@PathVariable Long id) {
		return partidoService.obtener(id);
	}

	@PutMapping("/{id}")
	public PartidoResponse actualizar(@PathVariable Long id, @Valid @RequestBody PartidoRequest request) {
		return partidoService.actualizar(id, request);
	}

	@PatchMapping("/{id}/estado")
	public PartidoResponse cambiarEstado(@PathVariable Long id, @RequestParam String valor) {
		return partidoService.cambiarEstado(id, valor);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		partidoService.eliminar(id);
	}

}
