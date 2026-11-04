package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record EquGruRequest(
        @NotNull Long idGrupo,
        @NotNull Long idTorneo,
        @NotNull Long idEquipo
) {
}
