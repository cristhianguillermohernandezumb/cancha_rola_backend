package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.TorUsuRequest;
import co.edu.unbosque.dto.TorUsuResponse;
import co.edu.unbosque.entity.TorUsu;
import co.edu.unbosque.entity.TorUsuId;
import org.springframework.stereotype.Component;

@Component
public class TorUsuMapper {

    public TorUsu toEntity(TorUsuRequest r) {
        TorUsu e = new TorUsu();
        // El servicio debe poner fechaSeguimiento antes de guardar.
        e.setId(new TorUsuId(r.idTorneo(), r.idUsuario()));
        e.setNotificacion(r.notificacion());
        return e;
    }

    public TorUsuResponse toResponse(TorUsu e) {
        return new TorUsuResponse(
                e.getId().getIdTorneo(),
                e.getTorneo() == null ? null : e.getTorneo().getNombre(),
                e.getId().getIdUsuario(),
                e.getFechaSeguimiento(),
                e.getNotificacion());
    }
}
