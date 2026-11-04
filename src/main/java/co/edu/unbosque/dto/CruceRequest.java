package co.edu.unbosque.dto;

/** Un enfrentamiento de la ronda: equipo local contra equipo visitante. */
public record CruceRequest(
        Long idLocal,
        Long idVisitante
) {
}
