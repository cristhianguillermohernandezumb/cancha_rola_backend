package co.edu.unbosque.dto;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long idAuditoria,
        String detalle,
        LocalDateTime fechaHora,
        String entidad,
        String idRegistro,
        String accion,
        Long idUsuario,
        String nombreUsuario,
        Long idTorneo
) {
}
