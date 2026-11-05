package co.edu.unbosque.security;

/** Datos del usuario que hizo la peticion (sale del token y de la base, nunca del cuerpo de la solicitud). */
public record UsuarioAutenticado(Long idUsuario, String correo, String rol) {
}
