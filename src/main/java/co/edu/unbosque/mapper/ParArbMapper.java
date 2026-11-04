package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.ParArbRequest;
import co.edu.unbosque.dto.ParArbResponse;
import co.edu.unbosque.entity.ParArb;
import co.edu.unbosque.entity.ParArbId;
import org.springframework.stereotype.Component;

@Component
public class ParArbMapper {

    public ParArb toEntity(ParArbRequest r) {
        ParArb e = new ParArb();
        e.setId(new ParArbId(r.idPartido(), r.idArbitro()));
        e.setRol(r.rol());
        return e;
    }

    public ParArbResponse toResponse(ParArb e) {
        return new ParArbResponse(
                e.getId().getIdPartido(),
                e.getId().getIdArbitro(),
                e.getArbitro() == null ? null : nombre(e.getArbitro().getPrimerNombre(), e.getArbitro().getPrimerApellido()),
                e.getRol());
    }

    private static String nombre(String primerNombre, String primerApellido) {
        return primerNombre + " " + primerApellido;
    }
}
