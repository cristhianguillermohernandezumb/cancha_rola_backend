package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.TorneoRequest;
import co.edu.unbosque.dto.TorneoResponse;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class TorneoMapper {

    public Torneo toEntity(TorneoRequest r, Usuario organizador) {
        Torneo e = new Torneo();
        // El servicio debe poner el estado inicial (INSCRIPCION) antes de guardar.
        e.setNombre(r.nombre());
        e.setFechaInicio(r.fechaInicio());
        e.setFechaFin(r.fechaFin());
        e.setTelefono(r.telefono());
        e.setModalidad(r.modalidad());
        e.setCategoria(r.categoria());
        e.setCostoInscripcion(r.costoInscripcion());
        e.setMaxEquipos(r.maxEquipos());
        e.setOrganizador(organizador);
        return e;
    }

    /** Copia los datos editables de la solicitud sobre un torneo existente (no toca id, estado ni organizador). */
    public void actualizarEntidad(Torneo e, TorneoRequest r) {
        e.setNombre(r.nombre());
        e.setFechaInicio(r.fechaInicio());
        e.setFechaFin(r.fechaFin());
        e.setTelefono(r.telefono());
        e.setModalidad(r.modalidad());
        e.setCategoria(r.categoria());
        e.setCostoInscripcion(r.costoInscripcion());
        e.setMaxEquipos(r.maxEquipos());
    }

    public TorneoResponse toResponse(Torneo e) {
        return new TorneoResponse(
                e.getIdTorneo(),
                e.getNombre(),
                e.getFechaInicio(),
                e.getFechaFin(),
                e.getEstado(),
                e.getTelefono(),
                e.getModalidad(),
                e.getCategoria(),
                e.getCostoInscripcion(),
                e.getMaxEquipos(),
                e.getOrganizador() == null ? null : e.getOrganizador().getIdUsuario(),
                e.getOrganizador() == null ? null : nombre(e.getOrganizador().getPrimerNombre(), e.getOrganizador().getPrimerApellido()));
    }

    private static String nombre(String primerNombre, String primerApellido) {
        return primerNombre + " " + primerApellido;
    }
}
