package co.edu.unbosque.dto;

public record ParEquResponse(
        Long idPartido,
        Long idEquipo,
        String nombreEquipo,
        Long idTorneo,
        String rol
) {
}
