package co.edu.unbosque.dto;

import java.util.List;

/**
 * Tanda de penales de un partido. aplica = partido de eliminatoria que termino empatado.
 * siguiente = quien patea el proximo tiro (LOCAL/VISITANTE, null si ya hay ganador);
 * ganador = LOCAL/VISITANTE cuando la tanda ya se decidio.
 */
public record PenalesResponse(
        Long idPartido,
        Long idLocal,
        Long idVisitante,
        Boolean aplica,
        List<TiroPenalResponse> tiros,
        Integer golesLocal,
        Integer golesVisitante,
        String siguiente,
        String ganador,
        Long idGanador
) {
}
