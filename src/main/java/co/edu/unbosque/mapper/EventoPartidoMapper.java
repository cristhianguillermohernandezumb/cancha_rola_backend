package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.EventoPartidoRequest;
import co.edu.unbosque.dto.EventoPartidoResponse;
import co.edu.unbosque.entity.EventoPartido;
import org.springframework.stereotype.Component;

@Component
public class EventoPartidoMapper {

    public EventoPartido toEntity(EventoPartidoRequest r) {
        EventoPartido e = new EventoPartido();
        e.setMinuto(r.minuto());
        e.setTipoEvento(r.tipoEvento());
        e.setIdPartido(r.idPartido());
        e.setIdEquipo(r.idEquipo());
        e.setIdTorneo(r.idTorneo());
        e.setIdJugador(r.idJugador());
        return e;
    }

    public EventoPartidoResponse toResponse(EventoPartido e) {
        return new EventoPartidoResponse(
                e.getIdEvento(),
                e.getMinuto(),
                e.getTipoEvento(),
                e.getIdPartido(),
                e.getIdEquipo(),
                e.getIdTorneo(),
                e.getIdJugador(),
                e.getAlineacion() == null || e.getAlineacion().getInscripcionJugador() == null || e.getAlineacion().getInscripcionJugador().getJugador() == null ? null : nombre(e.getAlineacion().getInscripcionJugador().getJugador().getPrimerNombre(), e.getAlineacion().getInscripcionJugador().getJugador().getPrimerApellido()));
    }

    private static String nombre(String primerNombre, String primerApellido) {
        return primerNombre + " " + primerApellido;
    }
}
