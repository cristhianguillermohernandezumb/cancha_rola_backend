package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.GrupoRequest;
import co.edu.unbosque.dto.GrupoResponse;
import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.entity.Torneo;
import org.springframework.stereotype.Component;

@Component
public class GrupoMapper {

    public Grupo toEntity(GrupoRequest r, Torneo torneo) {
        Grupo e = new Grupo();
        e.setFase(r.fase());
        e.setLetra(r.letra());
        e.setTorneo(torneo);
        return e;
    }

    /** Copia los datos editables de la solicitud sobre un grupo existente (no toca id ni torneo). */
    public void actualizarEntidad(Grupo e, GrupoRequest r) {
        e.setFase(r.fase());
        e.setLetra(r.letra());
    }

    public GrupoResponse toResponse(Grupo e) {
        return new GrupoResponse(
                e.getIdGrupo(),
                e.getFase(),
                e.getLetra(),
                e.getTorneo() == null ? null : e.getTorneo().getIdTorneo());
    }
}
