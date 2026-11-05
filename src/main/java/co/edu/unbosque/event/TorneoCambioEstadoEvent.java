package co.edu.unbosque.event;

/** Se publica cuando un torneo cambia de etapa (EN_CURSO o FINALIZADO) y el cambio ya quedo guardado. */
public record TorneoCambioEstadoEvent(Long idTorneo, String estado) {
}
