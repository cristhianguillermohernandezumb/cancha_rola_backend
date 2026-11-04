package co.edu.unbosque.mapper;

import org.springframework.stereotype.Component;

import co.edu.unbosque.dto.AuditoriaResponse;
import co.edu.unbosque.entity.Auditoria;

/** La auditoria solo la escribe el backend, por eso no tiene solicitud (Request). */
@Component
public class AuditoriaMapper {

    public AuditoriaResponse toResponse(Auditoria e) {
        return new AuditoriaResponse(
                e.getIdAuditoria(),
                e.getDetalle(),
                e.getFechaHora(),
                e.getEntidad(),
                e.getIdRegistro(),
                e.getAccion(),
                e.getUsuario() == null ? null : e.getUsuario().getIdUsuario(),
                e.getUsuario() == null ? null : e.getUsuario().getPrimerNombre() + " " + e.getUsuario().getPrimerApellido(),
                e.getTorneo() == null ? null : e.getTorneo().getIdTorneo());
    }
}
