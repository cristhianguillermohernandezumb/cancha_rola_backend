package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.EntrenadorRequest;
import co.edu.unbosque.dto.EntrenadorResponse;
import co.edu.unbosque.service.EntrenadorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/entrenadores")
public class EntrenadorController {

	private final EntrenadorService entrenadorService;

	public EntrenadorController(EntrenadorService entrenadorService) {
		this.entrenadorService = entrenadorService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EntrenadorResponse crear(@Valid @RequestBody EntrenadorRequest request) {
		return entrenadorService.crear(request);
	}

	/** Lista todos. Filtro opcional: /entrenadores?idEquipo=1 */
	@GetMapping
	public List<EntrenadorResponse> listar(@RequestParam(required = false) Long idEquipo) {
		return entrenadorService.listar(idEquipo);
	}

	@GetMapping("/{id}")
	public EntrenadorResponse obtener(@PathVariable Long id) {
		return entrenadorService.obtener(id);
	}

	/** Ejemplo: /entrenadores/documento/CC/80123456 */
	@GetMapping("/documento/{tipo}/{numero}")
	public EntrenadorResponse obtenerPorDocumento(@PathVariable String tipo, @PathVariable String numero) {
		return entrenadorService.obtenerPorDocumento(tipo, numero);
	}

	@PutMapping("/{id}")
	public EntrenadorResponse actualizar(@PathVariable Long id, @Valid @RequestBody EntrenadorRequest request) {
		return entrenadorService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		entrenadorService.eliminar(id);
	}

}
