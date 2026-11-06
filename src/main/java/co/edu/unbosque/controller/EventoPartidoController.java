package co.edu.unbosque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.EventoPartidoRequest;
import co.edu.unbosque.dto.EventoPartidoResponse;
import co.edu.unbosque.service.EventoPartidoService;
import jakarta.validation.Valid;

@RestController
public class EventoPartidoController {

	private final EventoPartidoService eventoService;

	public EventoPartidoController(EventoPartidoService eventoService) {
		this.eventoService = eventoService;
	}

	@PostMapping("/eventos")
	@ResponseStatus(HttpStatus.CREATED)
	public EventoPartidoResponse registrar(@Valid @RequestBody EventoPartidoRequest request) {
		return eventoService.registrar(request);
	}

	@GetMapping("/partidos/{idPartido}/eventos")
	public List<EventoPartidoResponse> listar(@PathVariable Long idPartido) {
		return eventoService.listarPorPartido(idPartido);
	}

	@GetMapping("/eventos/{id}")
	public EventoPartidoResponse obtener(@PathVariable Long id) {
		return eventoService.obtener(id);
	}

	@DeleteMapping("/eventos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		eventoService.eliminar(id);
	}

}
