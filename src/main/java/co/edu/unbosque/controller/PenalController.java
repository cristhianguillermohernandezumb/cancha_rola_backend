package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.PenalesResponse;
import co.edu.unbosque.service.PenalService;

/** Tanda de penales de los partidos de eliminatoria empatados. La lectura es publica; registrar exige ser el organizador. */
@RestController
public class PenalController {

	private final PenalService penalService;

	public PenalController(PenalService penalService) {
		this.penalService = penalService;
	}

	@GetMapping("/partidos/{idPartido}/penales")
	public PenalesResponse obtener(@PathVariable Long idPartido) {
		return penalService.obtener(idPartido);
	}

	@PostMapping("/partidos/{idPartido}/penales")
	public PenalesResponse registrar(@PathVariable Long idPartido, @RequestParam boolean convertido) {
		return penalService.registrar(idPartido, convertido);
	}

	@DeleteMapping("/partidos/{idPartido}/penales/ultimo")
	public PenalesResponse deshacerUltimo(@PathVariable Long idPartido) {
		return penalService.deshacerUltimo(idPartido);
	}

	@GetMapping("/torneos/{idTorneo}/penales")
	public List<PenalesResponse> listarDeTorneo(@PathVariable Long idTorneo) {
		return penalService.listarDeTorneo(idTorneo);
	}

}
