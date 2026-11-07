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

import co.edu.unbosque.dto.ParArbResponse;
import co.edu.unbosque.service.ParArbService;

@RestController
@RequestMapping("/partidos/{idPartido}/arbitros")
public class ParArbController {

	private final ParArbService parArbService;

	public ParArbController(ParArbService parArbService) {
		this.parArbService = parArbService;
	}

	/** Ejemplo: POST /partidos/1/arbitros/2?rol=CENTRAL */
	@PostMapping("/{idArbitro}")
	@ResponseStatus(HttpStatus.CREATED)
	public ParArbResponse asignar(@PathVariable Long idPartido, @PathVariable Long idArbitro,
			@RequestParam String rol) {
		return parArbService.asignar(idPartido, idArbitro, rol);
	}

	@GetMapping
	public List<ParArbResponse> listar(@PathVariable Long idPartido) {
		return parArbService.listarPorPartido(idPartido);
	}

	@DeleteMapping("/{idArbitro}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void quitar(@PathVariable Long idPartido, @PathVariable Long idArbitro) {
		parArbService.quitar(idPartido, idArbitro);
	}

}
