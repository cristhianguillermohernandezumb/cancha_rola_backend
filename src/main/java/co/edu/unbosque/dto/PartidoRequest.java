package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record PartidoRequest(
        @NotNull LocalDateTime fechaHora,
        @Pattern(regexp = "PROGRAMADO|EN_JUEGO|FINALIZADO|APLAZADO", message = "valores permitidos: PROGRAMADO, EN_JUEGO, FINALIZADO, APLAZADO") String estado,
        @NotBlank @Size(max = 50) String fase,
        @NotNull @Positive Integer jornada,
        @NotNull Long idCancha,
        @NotNull Long idTorneo
) {
}
