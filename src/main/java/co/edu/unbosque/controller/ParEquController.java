package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.ParEquResponse;
import co.edu.unbosque.service.ParEquService;

@RestController
@RequestMapping("/partidos/{idPartido}/equipos")
public class ParEquController {

	private final ParEquService parEquService;

	public ParEquController(ParEquService parEquService) {
		this.parEquService = parEquService;
	}

	@PostMapping("/{idEquipo}")
	@ResponseStatus(HttpStatus.CREATED)
	public ParEquResponse asignar(@PathVariable Long idPartido, @PathVariable Long idEquipo,
			@RequestParam String rol) {
		return parEquService.asignar(idPartido, idEquipo, rol);
	}

	@GetMapping
	public List<ParEquResponse> listar(@PathVariable Long idPartido) {
		return parEquService.listarPorPartido(idPartido);
	}

	@DeleteMapping("/{idEquipo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void quitar(@PathVariable Long idPartido, @PathVariable Long idEquipo) {
		parEquService.quitar(idPartido, idEquipo);
	}

}
