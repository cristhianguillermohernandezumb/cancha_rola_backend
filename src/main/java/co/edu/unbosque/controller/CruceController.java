package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.CruceRequest;
import co.edu.unbosque.dto.CruceResponse;
import co.edu.unbosque.service.CruceService;

/** Cruces de las rondas eliminatorias (por ahora 32AVOS). La lectura es publica; escribir exige ser el organizador. */
@RestController
public class CruceController {

	private final CruceService cruceService;

	public CruceController(CruceService cruceService) {
		this.cruceService = cruceService;
	}

	@GetMapping("/torneos/{idTorneo}/cruces")
	public List<CruceResponse> listar(@PathVariable Long idTorneo, @RequestParam(required = false) String ronda) {
		return cruceService.listar(idTorneo, ronda);
	}

	@PutMapping("/torneos/{idTorneo}/cruces/{ronda}")
	public List<CruceResponse> reemplazar(@PathVariable Long idTorneo, @PathVariable String ronda,
			@RequestBody List<CruceRequest> cruces) {
		return cruceService.reemplazar(idTorneo, ronda, cruces);
	}

	@DeleteMapping("/torneos/{idTorneo}/cruces/{ronda}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long idTorneo, @PathVariable String ronda) {
		cruceService.eliminar(idTorneo, ronda);
	}

}
