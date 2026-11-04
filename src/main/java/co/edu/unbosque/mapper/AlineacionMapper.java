package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.AlineacionRequest;
import co.edu.unbosque.dto.AlineacionResponse;
import co.edu.unbosque.entity.Alineacion;
import co.edu.unbosque.entity.AlineacionId;
import org.springframework.stereotype.Component;

@Component
public class AlineacionMapper {

    public Alineacion toEntity(AlineacionRequest r) {
        Alineacion e = new Alineacion();
        e.setId(new AlineacionId(r.idPartido(), r.idEquipo(), r.idTorneo(), r.idJugador()));
        e.setTitular(r.titular());
        e.setMinutosJugados(r.minutosJugados());
        return e;
    }

    public AlineacionResponse toResponse(Alineacion e) {
        return new AlineacionResponse(
                e.getId().getIdPartido(),
                e.getId().getIdEquipo(),
                e.getId().getIdTorneo(),
                e.getId().getIdJugador(),
                e.getInscripcionJugador() == null || e.getInscripcionJugador().getJugador() == null ? null : nombre(e.getInscripcionJugador().getJugador().getPrimerNombre(), e.getInscripcionJugador().getJugador().getPrimerApellido()),
                e.getTitular(),
                e.getMinutosJugados());
    }

    private static String nombre(String primerNombre, String primerApellido) {
        return primerNombre + " " + primerApellido;
    }
}
