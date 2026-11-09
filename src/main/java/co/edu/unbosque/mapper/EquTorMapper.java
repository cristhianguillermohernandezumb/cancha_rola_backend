package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.EquTorRequest;
import co.edu.unbosque.dto.EquTorResponse;
import co.edu.unbosque.entity.EquTor;
import co.edu.unbosque.entity.EquTorId;
import org.springframework.stereotype.Component;

@Component
public class EquTorMapper {

    public EquTor toEntity(EquTorRequest r) {
        EquTor e = new EquTor();
        e.setId(new EquTorId(r.idEquipo(), r.idTorneo()));
        return e;
    }

    public EquTorResponse toResponse(EquTor e) {
        return new EquTorResponse(
                e.getId().getIdEquipo(),
                e.getEquipo() == null ? null : e.getEquipo().getNombre(),
                e.getId().getIdTorneo(),
                e.getTorneo() == null ? null : e.getTorneo().getNombre(),
                e.getEstadoInscripcion(),
                e.getFechaInscripcion());
    }
}
