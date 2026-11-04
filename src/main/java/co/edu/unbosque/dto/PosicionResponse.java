package co.edu.unbosque.dto;

/** Una fila de la tabla de posiciones. Victoria = 3 puntos, empate = 1, derrota = 0. */
public record PosicionResponse(
        Integer posicion,
        Long idEquipo,
        String nombreEquipo,
        Integer partidosJugados,
        Integer ganados,
        Integer empatados,
        Integer perdidos,
        Integer golesFavor,
        Integer golesContra,
        Integer diferenciaGoles,
        Integer puntos
) {
}
