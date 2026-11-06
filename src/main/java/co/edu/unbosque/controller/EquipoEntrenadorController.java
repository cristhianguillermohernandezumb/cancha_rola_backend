package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.EntrenadorPublicoResponse;
import co.edu.unbosque.service.EntrenadorService;

/** Vista publica de los entrenadores de un equipo inscrito en un torneo (para la pagina del equipo). */
@RestController
public class EquipoEntrenadorController {

	private final EntrenadorService entrenadorService;

	public EquipoEntrenadorController(EntrenadorService entrenadorService) {
		this.entrenadorService = entrenadorService;
	}

	@GetMapping("/torneos/{idTorneo}/equipos/{idEquipo}/entrenadores")
	public List<EntrenadorPublicoResponse> listar(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
		return entrenadorService.listarPublico(idTorneo, idEquipo);
	}

}
