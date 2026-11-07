package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.AlineacionResponse;
import co.edu.unbosque.dto.CopiaAlineacionResponse;
import co.edu.unbosque.service.AlineacionService;

@RestController
@RequestMapping("/partidos/{idPartido}")
public class AlineacionController {

	private final AlineacionService alineacionService;

	public AlineacionController(AlineacionService alineacionService) {
		this.alineacionService = alineacionService;
	}

	/** Ejemplo: POST /partidos/1/equipos/3/alineacion/10?titular=S */
	@PostMapping("/equipos/{idEquipo}/alineacion/{idJugador}")
	@ResponseStatus(HttpStatus.CREATED)
	public AlineacionResponse agregar(@PathVariable Long idPartido, @PathVariable Long idEquipo,
			@PathVariable Long idJugador, @RequestParam(defaultValue = "S") String titular) {
		return alineacionService.agregar(idPartido, idEquipo, idJugador, titular);
	}

	/** Copia la ultima alineacion usada por el equipo. Ejemplo: POST /partidos/1/equipos/3/alineacion/anterior */
	@PostMapping("/equipos/{idEquipo}/alineacion/anterior")
	@ResponseStatus(HttpStatus.CREATED)
	public CopiaAlineacionResponse copiarAnterior(@PathVariable Long idPartido, @PathVariable Long idEquipo) {
		return alineacionService.copiarAnterior(idPartido, idEquipo);
	}

	/** Alineacion de los dos equipos del partido. */
	@GetMapping("/alineacion")
	public List<AlineacionResponse> listarPartido(@PathVariable Long idPartido) {
		return alineacionService.listarPorPartido(idPartido);
	}

	/** Alineacion de un solo equipo. */
	@GetMapping("/equipos/{idEquipo}/alineacion")
	public List<AlineacionResponse> listarEquipo(@PathVariable Long idPartido, @PathVariable Long idEquipo) {
		return alineacionService.listarPorPartidoYEquipo(idPartido, idEquipo);
	}

	/** Ejemplo: PATCH /partidos/1/equipos/3/alineacion/10?titular=N  o  ?minutos=90 */
	@PatchMapping("/equipos/{idEquipo}/alineacion/{idJugador}")
	public AlineacionResponse actualizar(@PathVariable Long idPartido, @PathVariable Long idEquipo,
			@PathVariable Long idJugador, @RequestParam(required = false) String titular,
			@RequestParam(required = false) Integer minutos) {
		return alineacionService.actualizar(idPartido, idEquipo, idJugador, titular, minutos);
	}

	@DeleteMapping("/equipos/{idEquipo}/alineacion/{idJugador}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void quitar(@PathVariable Long idPartido, @PathVariable Long idEquipo, @PathVariable Long idJugador) {
		alineacionService.quitar(idPartido, idEquipo, idJugador);
	}

}
