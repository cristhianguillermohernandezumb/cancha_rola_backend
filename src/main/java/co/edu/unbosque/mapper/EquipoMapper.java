package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.EquipoRequest;
import co.edu.unbosque.dto.EquipoResponse;
import co.edu.unbosque.entity.Equipo;
import org.springframework.stereotype.Component;

@Component
public class EquipoMapper {

    public Equipo toEntity(EquipoRequest r) {
        Equipo e = new Equipo();
        e.setNombre(r.nombre());
        e.setFechaFundacion(r.fechaFundacion());
        e.setCorreo(r.correo());
        e.setTelefonoEquipo(r.telefonoEquipo());
        e.setEscudo(r.escudo());
        return e;
    }

    /** Copia los datos editables de la solicitud sobre un equipo existente (no toca el id). */
    public void actualizarEntidad(Equipo e, EquipoRequest r) {
        e.setNombre(r.nombre());
        e.setFechaFundacion(r.fechaFundacion());
        e.setCorreo(r.correo());
        e.setTelefonoEquipo(r.telefonoEquipo());
        // Un escudo de 1 byte es el relleno de "no cambiar": se conserva el escudo que ya tiene el equipo.
        if (r.escudo() != null && r.escudo().length > 1) {
            e.setEscudo(r.escudo());
        }
    }

    public EquipoResponse toResponse(Equipo e) {
        return new EquipoResponse(
                e.getIdEquipo(),
                e.getNombre(),
                e.getFechaFundacion(),
                e.getCorreo(),
                e.getTelefonoEquipo(),
                e.getEscudo());
    }
}
