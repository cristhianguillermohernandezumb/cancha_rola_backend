package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TorneoRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull @Positive Long telefono,
        @NotBlank @Pattern(regexp = "FUTBOL_5|FUTBOL_7|FUTBOL_8|FUTBOL_11", message = "valores permitidos: FUTBOL_5, FUTBOL_7, FUTBOL_8, FUTBOL_11") String modalidad,
        @NotBlank @Size(max = 20) String categoria,
        @NotNull @PositiveOrZero BigDecimal costoInscripcion,
        @NotNull @Positive Integer maxEquipos,
        @NotNull Long idOrganizador
) {
}
