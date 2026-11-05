package co.edu.unbosque.exception;

/** Se lanza cuando se rompe una regla del negocio, por ejemplo un correo repetido (responde 409). */
public class ReglaNegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReglaNegocioException(String mensaje) {
		super(mensaje);
	}

}
