package co.edu.unbosque.dto;

/** Un jugador en la tabla de goleadores (no cuenta los autogoles). */
public record GoleadorResponse(
        Integer posicion,
        Long idJugador,
        String nombreJugador,
        Long idEquipo,
        String nombreEquipo,
        Long goles
) {
}
