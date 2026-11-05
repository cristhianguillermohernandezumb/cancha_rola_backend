package co.edu.unbosque.exception;

/** El usuario esta identificado pero no tiene permiso para esta operacion (HTTP 403). */
public class AccesoDenegadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccesoDenegadoException(String mensaje) {
		super(mensaje);
	}

}
