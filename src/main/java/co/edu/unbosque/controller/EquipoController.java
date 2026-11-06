package co.edu.unbosque.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.dto.EquipoRequest;
import co.edu.unbosque.dto.EquipoResponse;
import co.edu.unbosque.service.EquipoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

	private final EquipoService equipoService;

	public EquipoController(EquipoService equipoService) {
		this.equipoService = equipoService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EquipoResponse crear(@Valid @RequestBody EquipoRequest request) {
		return equipoService.crear(request);
	}

	@GetMapping
	public List<EquipoResponse> listar(@RequestParam(required = false) String nombre) {
		return equipoService.listar(nombre);
	}

	@GetMapping("/{id}")
	public EquipoResponse obtener(@PathVariable Long id) {
		return equipoService.obtener(id);
	}

	@GetMapping("/{id}/escudo")
	public ResponseEntity<byte[]> escudo(@PathVariable Long id) {
		byte[] datos = equipoService.obtenerEscudo(id);
		String tipo = EquipoService.tipoDeImagen(datos);
		if (tipo == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(tipo))
				.cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).header("X-Content-Type-Options", "nosniff")
				.body(datos);
	}

	@PutMapping(value = "/{id}/escudo", consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cambiarEscudo(@PathVariable Long id, @RequestBody byte[] escudo) {
		equipoService.actualizarEscudo(id, escudo);
	}

	@PutMapping("/{id}")
	public EquipoResponse actualizar(@PathVariable Long id, @Valid @RequestBody EquipoRequest request) {
		return equipoService.actualizar(id, request);
	}

}
