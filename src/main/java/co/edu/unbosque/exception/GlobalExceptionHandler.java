package co.edu.unbosque.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Convierte las excepciones en respuestas JSON con un formato uniforme. */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoEncontradoException.class)
	public ResponseEntity<Map<String, Object>> noEncontrado(NoEncontradoException ex) {
		return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
	}

	@ExceptionHandler(ReglaNegocioException.class)
	public ResponseEntity<Map<String, Object>> reglaNegocio(ReglaNegocioException ex) {
		return respuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
	}

	@ExceptionHandler(AccesoDenegadoException.class)
	public ResponseEntity<Map<String, Object>> accesoDenegado(AccesoDenegadoException ex) {
		return respuesta(HttpStatus.FORBIDDEN, ex.getMessage(), null);
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<Map<String, Object>> credenciales(CredencialesInvalidasException ex) {
		return respuesta(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
		Map<String, String> errores = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
		return respuesta(HttpStatus.BAD_REQUEST, "Hay datos invalidos en la solicitud", errores);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException ex) {
		return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es un JSON valido", null);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> integridad(DataIntegrityViolationException ex) {
		return respuesta(HttpStatus.CONFLICT, "La operacion viola una restriccion de la base de datos", null);
	}

	private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado, String mensaje, Object detalles) {
		Map<String, Object> cuerpo = new LinkedHashMap<>();
		cuerpo.put("fecha", LocalDateTime.now().toString());
		cuerpo.put("estado", estado.value());
		cuerpo.put("mensaje", mensaje);
		if (detalles != null) {
			cuerpo.put("detalles", detalles);
		}
		return ResponseEntity.status(estado).body(cuerpo);
	}

}
