package co.edu.unbosque.dto;

import java.time.LocalDate;

public record EquTorResponse(
        Long idEquipo,
        String nombreEquipo,
        Long idTorneo,
        String nombreTorneo,
        String estadoInscripcion,
        LocalDate fechaInscripcion
) {
}
