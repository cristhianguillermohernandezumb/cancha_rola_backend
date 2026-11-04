package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.InscripcionJugadorRequest;
import co.edu.unbosque.dto.InscripcionJugadorResponse;
import co.edu.unbosque.entity.InscripcionJugador;
import co.edu.unbosque.entity.InscripcionJugadorId;
import org.springframework.stereotype.Component;

@Component
public class InscripcionJugadorMapper {

    public InscripcionJugador toEntity(InscripcionJugadorRequest r) {
        InscripcionJugador e = new InscripcionJugador();
        e.setId(new InscripcionJugadorId(r.idJugador(), r.idEquipo(), r.idTorneo()));
        e.setNumeroCamiseta(r.numeroCamiseta());
        return e;
    }

    public InscripcionJugadorResponse toResponse(InscripcionJugador e) {
        return new InscripcionJugadorResponse(
                e.getId().getIdJugador(),
                e.getJugador() == null ? null : nombre(e.getJugador().getPrimerNombre(), e.getJugador().getPrimerApellido()),
                e.getId().getIdEquipo(),
                e.getId().getIdTorneo(),
                e.getNumeroCamiseta());
    }

    private static String nombre(String primerNombre, String primerApellido) {
        return primerNombre + " " + primerApellido;
    }
}
