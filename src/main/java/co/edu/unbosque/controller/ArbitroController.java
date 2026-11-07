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

import co.edu.unbosque.dto.ArbitroRequest;
import co.edu.unbosque.dto.ArbitroResponse;
import co.edu.unbosque.service.ArbitroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/arbitros")
public class ArbitroController {

	private final ArbitroService arbitroService;

	public ArbitroController(ArbitroService arbitroService) {
		this.arbitroService = arbitroService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ArbitroResponse crear(@Valid @RequestBody ArbitroRequest request) {
		return arbitroService.crear(request);
	}

	/** Lista todos. Filtro opcional: /arbitros?estado=ACTIVO */
	@GetMapping
	public List<ArbitroResponse> listar(@RequestParam(required = false) String estado) {
		return arbitroService.listar(estado);
	}

	@GetMapping("/{id}")
	public ArbitroResponse obtener(@PathVariable Long id) {
		return arbitroService.obtener(id);
	}

	/** Ejemplo: /arbitros/documento/CC/79123456 */
	@GetMapping("/documento/{tipo}/{numero}")
	public ArbitroResponse obtenerPorDocumento(@PathVariable String tipo, @PathVariable String numero) {
		return arbitroService.obtenerPorDocumento(tipo, numero);
	}

	@PutMapping("/{id}")
	public ArbitroResponse actualizar(@PathVariable Long id, @Valid @RequestBody ArbitroRequest request) {
		return arbitroService.actualizar(id, request);
	}

	/** Ejemplo: /arbitros/1/estado?valor=INACTIVO */
	@PatchMapping("/{id}/estado")
	public ArbitroResponse cambiarEstado(@PathVariable Long id, @RequestParam String valor) {
		return arbitroService.cambiarEstado(id, valor);
	}

}
