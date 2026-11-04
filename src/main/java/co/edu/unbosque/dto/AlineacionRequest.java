package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record AlineacionRequest(
        @NotNull Long idPartido,
        @NotNull Long idEquipo,
        @NotNull Long idTorneo,
        @NotNull Long idJugador,
        @NotBlank @Pattern(regexp = "S|N", message = "valores permitidos: S, N") String titular,
        @NotNull @PositiveOrZero Integer minutosJugados
) {
}
