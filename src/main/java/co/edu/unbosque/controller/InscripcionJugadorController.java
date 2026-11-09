package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.InscripcionJugadorRequest;
import co.edu.unbosque.dto.InscripcionJugadorResponse;
import co.edu.unbosque.service.InscripcionJugadorService;
import jakarta.validation.Valid;

/** Jugadores inscritos en un equipo dentro de un torneo. */
@RestController
public class InscripcionJugadorController {

	private final InscripcionJugadorService inscripcionService;

	public InscripcionJugadorController(InscripcionJugadorService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}

	@PostMapping("/inscripciones-jugador")
	@ResponseStatus(HttpStatus.CREATED)
	public InscripcionJugadorResponse inscribir(@Valid @RequestBody InscripcionJugadorRequest request) {
		return inscripcionService.inscribir(request);
	}

	@GetMapping("/torneos/{idTorneo}/equipos/{idEquipo}/jugadores")
	public List<InscripcionJugadorResponse> listarPorEquipoYTorneo(@PathVariable Long idTorneo,
			@PathVariable Long idEquipo) {
		return inscripcionService.listarPorEquipoYTorneo(idTorneo, idEquipo);
	}

	@GetMapping("/jugadores/{idJugador}/inscripciones")
	public List<InscripcionJugadorResponse> listarPorJugador(@PathVariable Long idJugador) {
		return inscripcionService.listarPorJugador(idJugador);
	}

	@PatchMapping("/torneos/{idTorneo}/equipos/{idEquipo}/jugadores/{idJugador}/camiseta")
	public InscripcionJugadorResponse cambiarCamiseta(@PathVariable Long idTorneo, @PathVariable Long idEquipo,
			@PathVariable Long idJugador, @RequestParam Integer numero) {
		return inscripcionService.cambiarCamiseta(idTorneo, idEquipo, idJugador, numero);
	}

	@DeleteMapping("/torneos/{idTorneo}/equipos/{idEquipo}/jugadores/{idJugador}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void retirar(@PathVariable Long idTorneo, @PathVariable Long idEquipo, @PathVariable Long idJugador) {
		inscripcionService.retirar(idTorneo, idEquipo, idJugador);
	}

}
