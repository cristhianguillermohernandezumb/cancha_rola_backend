package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.AuditoriaResponse;
import co.edu.unbosque.service.AuditoriaService;

/** Solo lectura: los registros los crea el backend automaticamente. */
@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

	private final AuditoriaService auditoriaService;

	public AuditoriaController(AuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}

	/** Ejemplos: /auditoria, /auditoria?idUsuario=1, /auditoria?idTorneo=2 */
	@GetMapping
	public List<AuditoriaResponse> listar(@RequestParam(required = false) Long idUsuario,
			@RequestParam(required = false) Long idTorneo) {
		return auditoriaService.listar(idUsuario, idTorneo);
	}

}
