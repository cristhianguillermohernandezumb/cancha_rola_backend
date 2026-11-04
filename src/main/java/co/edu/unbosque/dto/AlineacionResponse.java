package co.edu.unbosque.dto;

public record AlineacionResponse(
        Long idPartido,
        Long idEquipo,
        Long idTorneo,
        Long idJugador,
        String nombreJugador,
        String titular,
        Integer minutosJugados
) {
}
