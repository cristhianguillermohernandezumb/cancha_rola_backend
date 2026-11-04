package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record UsuarioRequest(
        @NotBlank @Size(max = 50) String primerNombre,
        @NotNull @Size(max = 50) String segundoNombre,
        @NotBlank @Size(max = 50) String primerApellido,
        @NotNull @Size(max = 50) String segundoApellido,
        @Positive Long telefono,
        @NotBlank @Email @Size(max = 100) String correo,
        @NotBlank @Size(min = 8, max = 72) String contrasenia,
        @NotBlank @Pattern(regexp = "ORGANIZADOR|ESPECTADOR", message = "valores permitidos: ORGANIZADOR, ESPECTADOR") String rol
) {
}
