package co.edu.unbosque.dto;

public record InscripcionJugadorResponse(
        Long idJugador,
        String nombreJugador,
        Long idEquipo,
        Long idTorneo,
        Integer numeroCamiseta,
        String piernaHabil
) {
}
