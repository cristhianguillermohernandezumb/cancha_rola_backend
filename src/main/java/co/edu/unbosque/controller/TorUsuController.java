package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.TorUsuResponse;
import co.edu.unbosque.service.TorUsuService;

@RestController
public class TorUsuController {

	private final TorUsuService torUsuService;

	public TorUsuController(TorUsuService torUsuService) {
		this.torUsuService = torUsuService;
	}

	/** Ejemplo: POST /usuarios/5/seguimientos/1?notificacion=S (por defecto S) */
	@PostMapping("/usuarios/{idUsuario}/seguimientos/{idTorneo}")
	@ResponseStatus(HttpStatus.CREATED)
	public TorUsuResponse seguir(@PathVariable Long idUsuario, @PathVariable Long idTorneo,
			@RequestParam(defaultValue = "S") String notificacion) {
		return torUsuService.seguir(idUsuario, idTorneo, notificacion);
	 }

	/** Torneos que sigue un usuario. */
	@GetMapping("/usuarios/{idUsuario}/seguimientos")
	public List<TorUsuResponse> listarPorUsuario(@PathVariable Long idUsuario) {
		return torUsuService.listarPorUsuario(idUsuario);
	}

	/** Usuarios que siguen un torneo. */
	@GetMapping("/torneos/{idTorneo}/seguidores")
	public List<TorUsuResponse> listarPorTorneo(@PathVariable Long idTorneo) {
		return torUsuService.listarPorTorneo(idTorneo);
	}

	/** Ejemplo: PATCH /usuarios/5/seguimientos/1/notificacion?valor=N */
	@PatchMapping("/usuarios/{idUsuario}/seguimientos/{idTorneo}/notificacion")
	public TorUsuResponse cambiarNotificacion(@PathVariable Long idUsuario, @PathVariable Long idTorneo,
			@RequestParam String valor) {
		return torUsuService.cambiarNotificacion(idUsuario, idTorneo, valor);
	}

	@DeleteMapping("/usuarios/{idUsuario}/seguimientos/{idTorneo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void dejarDeSeguir(@PathVariable Long idUsuario, @PathVariable Long idTorneo) {
		torUsuService.dejarDeSeguir(idUsuario, idTorneo);
	}

}
