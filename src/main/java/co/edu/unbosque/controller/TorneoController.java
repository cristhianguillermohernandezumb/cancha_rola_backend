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

import co.edu.unbosque.dto.TorneoRequest;
import co.edu.unbosque.dto.TorneoResponse;
import co.edu.unbosque.service.TorneoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/torneos")
public class TorneoController {

	private final TorneoService torneoService;

	public TorneoController(TorneoService torneoService) {
		this.torneoService = torneoService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TorneoResponse crear(@Valid @RequestBody TorneoRequest request) {
		return torneoService.crear(request);
	}

	@GetMapping
	public List<TorneoResponse> listar(@RequestParam(required = false) String estado) {
		return torneoService.listar(estado);
	}

	@GetMapping("/{id}")
	public TorneoResponse obtener(@PathVariable Long id) {
		return torneoService.obtener(id);
	}

	@GetMapping("/organizador/{idUsuario}")
	public List<TorneoResponse> listarPorOrganizador(@PathVariable Long idUsuario) {
		return torneoService.listarPorOrganizador(idUsuario);
	}

	@PutMapping("/{id}")
	public TorneoResponse actualizar(@PathVariable Long id, @Valid @RequestBody TorneoRequest request) {
		return torneoService.actualizar(id, request);
	}

	@PatchMapping("/{id}/estado")
	public TorneoResponse cambiarEstado(@PathVariable Long id, @RequestParam String valor) {
		return torneoService.cambiarEstado(id, valor);
	}

}
