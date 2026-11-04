package co.edu.unbosque.dto;

public record CruceResponse(
        Long idCruce,
        Long idTorneo,
        String ronda,
        Integer orden,
        Long idLocal,
        String nombreLocal,
        Long idVisitante,
        String nombreVisitante
) {
}
