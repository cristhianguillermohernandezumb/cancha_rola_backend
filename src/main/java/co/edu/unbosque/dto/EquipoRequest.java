package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EquipoRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotNull @PastOrPresent LocalDate fechaFundacion,
        @NotBlank @Email @Size(max = 100) String correo,
        @NotNull @Positive Long telefonoEquipo,
        @NotNull byte[] escudo
) {
}
