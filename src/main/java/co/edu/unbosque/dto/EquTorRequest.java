package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record EquTorRequest(
        @NotNull Long idEquipo,
        @NotNull Long idTorneo
) {
}
