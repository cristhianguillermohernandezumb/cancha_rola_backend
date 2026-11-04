package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.EquGruRequest;
import co.edu.unbosque.dto.EquGruResponse;
import co.edu.unbosque.entity.EquGru;
import co.edu.unbosque.entity.EquGruId;
import org.springframework.stereotype.Component;

@Component
public class EquGruMapper {

    public EquGru toEntity(EquGruRequest r) {
        EquGru e = new EquGru();
        e.setId(new EquGruId(r.idGrupo(), r.idTorneo(), r.idEquipo()));
        return e;
    }

    public EquGruResponse toResponse(EquGru e) {
        return new EquGruResponse(
                e.getId().getIdGrupo(),
                e.getGrupo() == null ? null : e.getGrupo().getLetra(),
                e.getId().getIdTorneo(),
                e.getId().getIdEquipo(),
                e.getEquTor() == null || e.getEquTor().getEquipo() == null ? null : e.getEquTor().getEquipo().getNombre());
    }
}
