package co.edu.unbosque.exception;

/** Correo o contrasena incorrectos, o usuario inactivo (HTTP 401). */
public class CredencialesInvalidasException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CredencialesInvalidasException(String mensaje) {
		super(mensaje);
	}

}
