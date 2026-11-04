package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record TorUsuRequest(
        @NotNull Long idTorneo,
        @NotNull Long idUsuario,
        @NotBlank @Pattern(regexp = "S|N", message = "valores permitidos: S, N") String notificacion
) {
}
