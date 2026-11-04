package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.EntrenadorPublicoResponse;
import co.edu.unbosque.dto.EntrenadorRequest;
import co.edu.unbosque.dto.EntrenadorResponse;
import co.edu.unbosque.entity.Entrenador;
import co.edu.unbosque.entity.Equipo;
import java.util.HashSet;
import org.springframework.stereotype.Component;

@Component
public class EntrenadorMapper {

    public Entrenador toEntity(EntrenadorRequest r, Equipo equipo) {
        Entrenador e = new Entrenador();
        e.setDocumento(r.documento());
        e.setTipoDocumento(r.tipoDocumento());
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setFechaNacimiento(r.fechaNacimiento());
        e.setAniosExperiencia(r.aniosExperiencia());
        e.setNacionalidad(r.nacionalidad());
        e.setEquipo(equipo);
        e.getCorreos().addAll(r.correos());
        e.getTelefonos().addAll(r.telefonos());
        return e;
    }

    /** Copia los datos de la solicitud sobre un entrenador existente (no toca el id). Reemplaza correos y telefonos. */
    public void actualizarEntidad(Entrenador e, EntrenadorRequest r, Equipo equipo) {
        e.setDocumento(r.documento());
        e.setTipoDocumento(r.tipoDocumento());
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setFechaNacimiento(r.fechaNacimiento());
        e.setAniosExperiencia(r.aniosExperiencia());
        e.setNacionalidad(r.nacionalidad());
        e.setEquipo(equipo);
        e.getCorreos().clear();
        e.getCorreos().addAll(r.correos());
        e.getTelefonos().clear();
        e.getTelefonos().addAll(r.telefonos());
    }

    /** Solo los datos que pueden verse sin iniciar sesion. */
    public EntrenadorPublicoResponse toPublico(Entrenador e) {
        return new EntrenadorPublicoResponse(
                e.getIdEntrenador(),
                e.getPrimerNombre(),
                e.getSegundoNombre(),
                e.getPrimerApellido(),
                e.getSegundoApellido(),
                e.getAniosExperiencia(),
                e.getNacionalidad());
    }

    public EntrenadorResponse toResponse(Entrenador e) {
        return new EntrenadorResponse(
                e.getIdEntrenador(),
                e.getDocumento(),
                e.getTipoDocumento(),
                e.getPrimerNombre(),
                e.getSegundoNombre(),
                e.getPrimerApellido(),
                e.getSegundoApellido(),
                e.getFechaNacimiento(),
                e.getAniosExperiencia(),
                e.getNacionalidad(),
                e.getEquipo() == null ? null : e.getEquipo().getIdEquipo(),
                e.getEquipo() == null ? null : e.getEquipo().getNombre(),
                new HashSet<>(e.getCorreos()),
                new HashSet<>(e.getTelefonos()));
    }
}
