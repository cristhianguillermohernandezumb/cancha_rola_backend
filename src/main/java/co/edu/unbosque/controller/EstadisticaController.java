package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.GoleadorResponse;
import co.edu.unbosque.dto.PosicionResponse;
import co.edu.unbosque.service.EstadisticaService;

/** Consultas publicas (solo lectura) calculadas a partir de los partidos y eventos. */
@RestController
@RequestMapping("/torneos/{idTorneo}")
public class EstadisticaController {

	private final EstadisticaService estadisticaService;

	public EstadisticaController(EstadisticaService estadisticaService) {
		this.estadisticaService = estadisticaService;
	}

	/** Ejemplos: /torneos/1/posiciones  o  /torneos/1/posiciones?idGrupo=2 */
	@GetMapping("/posiciones")
	public List<PosicionResponse> posiciones(@PathVariable Long idTorneo,
			@RequestParam(required = false) Long idGrupo) {
		return estadisticaService.tablaDePosiciones(idTorneo, idGrupo);
	}

	/**
	 * Ejemplos: /torneos/1/goleadores, /torneos/1/goleadores?limite=5 o
	 * /torneos/1/goleadores?fase=grupos (solo grupos) / ?fase=eliminatoria (despues de grupos).
	 */
	@GetMapping("/goleadores")
	public List<GoleadorResponse> goleadores(@PathVariable Long idTorneo,
			@RequestParam(defaultValue = "10") int limite, @RequestParam(defaultValue = "todas") String fase) {
		return estadisticaService.goleadores(idTorneo, limite, fase);
	}

}
