package co.edu.unbosque.dto;

import java.time.LocalDate;

public record UsuarioResponse(
        Long idUsuario,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        LocalDate fechaRegistro,
        String rol,
        Long telefono,
        String correo,
        String estado
) {
}
