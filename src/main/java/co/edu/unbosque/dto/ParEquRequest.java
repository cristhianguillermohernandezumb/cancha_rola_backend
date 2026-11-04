package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record ParEquRequest(
        @NotNull Long idPartido,
        @NotNull Long idEquipo,
        @NotNull Long idTorneo,
        @NotBlank @Pattern(regexp = "LOCAL|VISITANTE", message = "valores permitidos: LOCAL, VISITANTE") String rol
) {
}
