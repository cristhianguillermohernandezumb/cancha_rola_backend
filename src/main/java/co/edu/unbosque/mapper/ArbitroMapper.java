package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.ArbitroRequest;
import co.edu.unbosque.dto.ArbitroResponse;
import co.edu.unbosque.entity.Arbitro;
import java.util.HashSet;
import org.springframework.stereotype.Component;

@Component
public class ArbitroMapper {

    public Arbitro toEntity(ArbitroRequest r) {
        Arbitro e = new Arbitro();
        e.setDocumento(r.documento());
        e.setTipoDocumento(r.tipoDocumento());
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setFechaNacimiento(r.fechaNacimiento());
        e.setNumeroLicencia(r.numeroLicencia());
        e.setNacionalidad(r.nacionalidad());
        e.setEstado(r.estado());
        e.getCorreos().addAll(r.correos());
        e.getTelefonos().addAll(r.telefonos());
        return e;
    }

    /** Copia los datos de la solicitud sobre un arbitro existente (no toca el id). Reemplaza correos y telefonos. */
    public void actualizarEntidad(Arbitro e, ArbitroRequest r) {
        e.setDocumento(r.documento());
        e.setTipoDocumento(r.tipoDocumento());
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setFechaNacimiento(r.fechaNacimiento());
        e.setNumeroLicencia(r.numeroLicencia());
        e.setNacionalidad(r.nacionalidad());
        e.setEstado(r.estado());
        e.getCorreos().clear();
        e.getCorreos().addAll(r.correos());
        e.getTelefonos().clear();
        e.getTelefonos().addAll(r.telefonos());
    }

    public ArbitroResponse toResponse(Arbitro e) {
        return new ArbitroResponse(
                e.getIdArbitro(),
                e.getDocumento(),
                e.getTipoDocumento(),
                e.getPrimerNombre(),
                e.getSegundoNombre(),
                e.getPrimerApellido(),
                e.getSegundoApellido(),
                e.getFechaNacimiento(),
                e.getNumeroLicencia(),
                e.getNacionalidad(),
                e.getEstado(),
                new HashSet<>(e.getCorreos()),
                new HashSet<>(e.getTelefonos()));
    }
}
