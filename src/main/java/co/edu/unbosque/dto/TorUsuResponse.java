package co.edu.unbosque.dto;

import java.time.LocalDate;

public record TorUsuResponse(
        Long idTorneo,
        String nombreTorneo,
        Long idUsuario,
        LocalDate fechaSeguimiento,
        String notificacion
) {
}
