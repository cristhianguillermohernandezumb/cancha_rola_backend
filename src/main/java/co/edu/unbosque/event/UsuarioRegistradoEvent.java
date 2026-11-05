package co.edu.unbosque.event;

/** Se publica cuando un usuario nuevo queda guardado en la base. */
public record UsuarioRegistradoEvent(Long idUsuario, String nombre, String correo) {
}
