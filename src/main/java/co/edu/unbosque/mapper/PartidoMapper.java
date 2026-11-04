package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.PartidoRequest;
import co.edu.unbosque.dto.PartidoResponse;
import co.edu.unbosque.entity.Cancha;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.entity.Torneo;
import org.springframework.stereotype.Component;

@Component
public class PartidoMapper {

    public Partido toEntity(PartidoRequest r, Cancha cancha, Torneo torneo) {
        Partido e = new Partido();
        // El estado se deja en lo que diga la solicitud o PROGRAMADO si viene vacio (lo decide el servicio).
        if (r.estado() != null) {
            e.setEstado(r.estado());
        }
        e.setFechaHora(r.fechaHora());
        e.setFase(r.fase());
        e.setJornada(r.jornada());
        e.setCancha(cancha);
        e.setTorneo(torneo);
        return e;
    }

    /** Copia los datos editables sobre un partido existente. El torneo no se cambia ni se tocan los goles. */
    public void actualizarEntidad(Partido e, PartidoRequest r, Cancha cancha) {
        if (r.estado() != null) {
            e.setEstado(r.estado());
        }
        e.setFechaHora(r.fechaHora());
        e.setFase(r.fase());
        e.setJornada(r.jornada());
        e.setCancha(cancha);
    }

    public PartidoResponse toResponse(Partido e) {
        return new PartidoResponse(
                e.getIdPartido(),
                e.getFechaHora(),
                e.getEstado(),
                e.getGolesLocal(),
                e.getGolesVisitante(),
                e.getFase(),
                e.getJornada(),
                e.getCancha() == null ? null : e.getCancha().getIdCancha(),
                e.getCancha() == null ? null : e.getCancha().getNombre(),
                e.getTorneo() == null ? null : e.getTorneo().getIdTorneo());
    }
}
