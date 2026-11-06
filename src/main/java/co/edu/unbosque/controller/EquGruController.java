package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.EquGruResponse;
import co.edu.unbosque.service.EquGruService;

/** Equipos de cada grupo. */
@RestController
public class EquGruController {

	private final EquGruService equGruService;

	public EquGruController(EquGruService equGruService) {
		this.equGruService = equGruService;
	}

	@PostMapping("/grupos/{idGrupo}/equipos/{idEquipo}")
	@ResponseStatus(HttpStatus.CREATED)
	public EquGruResponse asignar(@PathVariable Long idGrupo, @PathVariable Long idEquipo) {
		return equGruService.asignar(idGrupo, idEquipo);
	}

	@GetMapping("/grupos/{idGrupo}/equipos")
	public List<EquGruResponse> listarPorGrupo(@PathVariable Long idGrupo) {
		return equGruService.listarPorGrupo(idGrupo);
	}

	@DeleteMapping("/grupos/{idGrupo}/equipos/{idEquipo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void quitar(@PathVariable Long idGrupo, @PathVariable Long idEquipo) {
		equGruService.quitar(idGrupo, idEquipo);
	}

}
