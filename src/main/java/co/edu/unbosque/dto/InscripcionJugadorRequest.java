package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record InscripcionJugadorRequest(
        @NotNull Long idJugador,
        @NotNull Long idEquipo,
        @NotNull Long idTorneo,
        @NotNull @Min(0) @Max(99) Integer numeroCamiseta
) {
}
