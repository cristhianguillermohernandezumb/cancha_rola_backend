package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record EventoPartidoRequest(
        @NotNull @PositiveOrZero Integer minuto,
        @NotBlank @Pattern(regexp = "GOL|AUTOGOL|AMARILLA|ROJA|FALTA", message = "valores permitidos: GOL, AUTOGOL, AMARILLA, ROJA, FALTA") String tipoEvento,
        @NotNull Long idPartido,
        @NotNull Long idEquipo,
        @NotNull Long idTorneo,
        @NotNull Long idJugador
) {
}
