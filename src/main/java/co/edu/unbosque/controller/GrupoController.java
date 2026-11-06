package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.GrupoRequest;
import co.edu.unbosque.dto.GrupoResponse;
import co.edu.unbosque.service.GrupoService;
import jakarta.validation.Valid;

@RestController
public class GrupoController {

	private final GrupoService grupoService;

	public GrupoController(GrupoService grupoService) {
		this.grupoService = grupoService;
	}

	@PostMapping("/grupos")
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoResponse crear(@Valid @RequestBody GrupoRequest request) {
		return grupoService.crear(request);
	}

	@GetMapping("/torneos/{idTorneo}/grupos")
	public List<GrupoResponse> listarPorTorneo(@PathVariable Long idTorneo) {
		return grupoService.listarPorTorneo(idTorneo);
	}

	@GetMapping("/grupos/{id}")
	public GrupoResponse obtener(@PathVariable Long id) {
		return grupoService.obtener(id);
	}

	@PutMapping("/grupos/{id}")
	public GrupoResponse actualizar(@PathVariable Long id, @Valid @RequestBody GrupoRequest request) {
		return grupoService.actualizar(id, request);
	}

	@DeleteMapping("/grupos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		grupoService.eliminar(id);
	}

}
