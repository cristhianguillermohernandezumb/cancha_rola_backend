package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.ParEquRequest;
import co.edu.unbosque.dto.ParEquResponse;
import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.entity.ParEquId;
import org.springframework.stereotype.Component;

@Component
public class ParEquMapper {

    public ParEqu toEntity(ParEquRequest r) {
        ParEqu e = new ParEqu();
        e.setId(new ParEquId(r.idPartido(), r.idEquipo(), r.idTorneo()));
        e.setRol(r.rol());
        return e;
    }

    public ParEquResponse toResponse(ParEqu e) {
        return new ParEquResponse(
                e.getId().getIdPartido(),
                e.getId().getIdEquipo(),
                e.getEquTor() == null || e.getEquTor().getEquipo() == null ? null : e.getEquTor().getEquipo().getNombre(),
                e.getId().getIdTorneo(),
                e.getRol());
    }
}
