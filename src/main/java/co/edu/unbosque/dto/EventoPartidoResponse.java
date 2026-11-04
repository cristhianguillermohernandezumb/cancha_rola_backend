package co.edu.unbosque.dto;

public record EventoPartidoResponse(
        Long idEvento,
        Integer minuto,
        String tipoEvento,
        Long idPartido,
        Long idEquipo,
        Long idTorneo,
        Long idJugador,
        String nombreJugador
) {
}
