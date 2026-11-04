package co.edu.unbosque.dto;

import java.time.LocalDateTime;

public record PartidoResponse(
        Long idPartido,
        LocalDateTime fechaHora,
        String estado,
        Integer golesLocal,
        Integer golesVisitante,
        String fase,
        Integer jornada,
        Long idCancha,
        String nombreCancha,
        Long idTorneo
) {
}
