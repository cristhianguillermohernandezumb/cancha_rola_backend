package co.edu.unbosque.dto;

import java.util.List;

/**
 * Resultado de copiar la ultima alineacion de un equipo: jugadores agregados, cuantos no se pudieron copiar
 * (no inscritos en este torneo o ya en el partido) y el partido del que se copio.
 */
public record CopiaAlineacionResponse(
        Long idPartidoOrigen,
        List<AlineacionResponse> agregados,
        int omitidos
) {
}
