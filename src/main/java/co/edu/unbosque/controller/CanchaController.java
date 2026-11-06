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

import co.edu.unbosque.dto.CanchaRequest;
import co.edu.unbosque.dto.CanchaResponse;
import co.edu.unbosque.service.CanchaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/canchas")
public class CanchaController {

	private final CanchaService canchaService;

	public CanchaController(CanchaService canchaService) {
		this.canchaService = canchaService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CanchaResponse crear(@Valid @RequestBody CanchaRequest request) {
		return canchaService.crear(request);
	}

	@GetMapping
	public List<CanchaResponse> listar(@RequestParam(required = false) String localidad,
			@RequestParam(required = false) String tipoSuperficie) {
		return canchaService.listar(localidad, tipoSuperficie);
	}

	@GetMapping("/{id}")
	public CanchaResponse obtener(@PathVariable Long id) {
		return canchaService.obtener(id);
	}

	@PutMapping("/{id}")
	public CanchaResponse actualizar(@PathVariable Long id, @Valid @RequestBody CanchaRequest request) {
		return canchaService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		canchaService.eliminar(id);
	}

}
