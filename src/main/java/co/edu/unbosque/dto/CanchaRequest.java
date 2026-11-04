package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record CanchaRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(max = 50) String direccion,
        @NotBlank @Pattern(regexp = "SINTETICA|NATURAL|CEMENTO", message = "valores permitidos: SINTETICA, NATURAL, CEMENTO") String tipoSuperficie,
        @NotBlank @Size(max = 50) String localidad,
        @NotNull @Positive Integer capacidad,
        @NotNull @Positive Integer largo,
        @NotNull @Positive Integer ancho
) {
}
