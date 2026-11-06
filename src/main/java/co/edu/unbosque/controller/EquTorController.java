package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.EquTorResponse;
import co.edu.unbosque.service.EquTorService;

/** Inscripcion de equipos en torneos. */
@RestController
public class EquTorController {

	private final EquTorService equTorService;

	public EquTorController(EquTorService equTorService) {
		this.equTorService = equTorService;
	}

	@PostMapping("/torneos/{idTorneo}/equipos/{idEquipo}")
	@ResponseStatus(HttpStatus.CREATED)
	public EquTorResponse inscribir(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
		return equTorService.inscribir(idTorneo, idEquipo);
	}

	@GetMapping("/torneos/{idTorneo}/equipos")
	public List<EquTorResponse> listarPorTorneo(@PathVariable Long idTorneo) {
		return equTorService.listarPorTorneo(idTorneo);
	}

	@GetMapping("/equipos/{idEquipo}/torneos")
	public List<EquTorResponse> listarPorEquipo(@PathVariable Long idEquipo) {
		return equTorService.listarPorEquipo(idEquipo);
	}

	@PatchMapping("/torneos/{idTorneo}/equipos/{idEquipo}/pagar")
	public EquTorResponse marcarPagada(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
		return equTorService.marcarPagada(idTorneo, idEquipo);
	}

	@DeleteMapping("/torneos/{idTorneo}/equipos/{idEquipo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void retirar(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
		equTorService.retirar(idTorneo, idEquipo);
	}

}
