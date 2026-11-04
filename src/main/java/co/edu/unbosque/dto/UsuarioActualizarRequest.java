package co.edu.unbosque.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos que un usuario puede cambiar de su propio perfil. No incluye el rol (no se cambia desde aqui) y la
 * contrasenia es opcional: si no viene, se conserva la actual.
 */
public record UsuarioActualizarRequest(
        @NotBlank @Size(max = 50) String primerNombre,
        @NotNull @Size(max = 50) String segundoNombre,
        @NotBlank @Size(max = 50) String primerApellido,
        @NotNull @Size(max = 50) String segundoApellido,
        @Positive Long telefono,
        @NotBlank @Email @Size(max = 100) String correo,
        @Size(min = 8, max = 72) String contrasenia
) {
}
