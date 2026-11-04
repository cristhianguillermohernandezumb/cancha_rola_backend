package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.CanchaRequest;
import co.edu.unbosque.dto.CanchaResponse;
import co.edu.unbosque.entity.Cancha;
import org.springframework.stereotype.Component;

@Component
public class CanchaMapper {

    public Cancha toEntity(CanchaRequest r) {
        Cancha e = new Cancha();
        e.setNombre(r.nombre());
        e.setDireccion(r.direccion());
        e.setTipoSuperficie(r.tipoSuperficie());
        e.setLocalidad(r.localidad());
        e.setCapacidad(r.capacidad());
        e.setLargo(r.largo());
        e.setAncho(r.ancho());
        return e;
    }

    /** Copia los datos editables de la solicitud sobre una cancha existente (no toca el id). */
    public void actualizarEntidad(Cancha e, CanchaRequest r) {
        e.setNombre(r.nombre());
        e.setDireccion(r.direccion());
        e.setTipoSuperficie(r.tipoSuperficie());
        e.setLocalidad(r.localidad());
        e.setCapacidad(r.capacidad());
        e.setLargo(r.largo());
        e.setAncho(r.ancho());
    }

    public CanchaResponse toResponse(Cancha e) {
        return new CanchaResponse(
                e.getIdCancha(),
                e.getNombre(),
                e.getDireccion(),
                e.getTipoSuperficie(),
                e.getLocalidad(),
                e.getCapacidad(),
                e.getLargo(),
                e.getAncho());
    }
}
