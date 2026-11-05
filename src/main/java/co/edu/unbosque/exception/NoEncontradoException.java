package co.edu.unbosque.exception;

/** Se lanza cuando un registro buscado por id no existe (responde 404). */
public class NoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoEncontradoException(String mensaje) {
		super(mensaje);
	}

}
