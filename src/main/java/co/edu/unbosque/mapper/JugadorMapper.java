package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.JugadorRequest;
import co.edu.unbosque.dto.JugadorResponse;
import co.edu.unbosque.entity.Jugador;
import java.util.HashSet;
import org.springframework.stereotype.Component;

@Component
public class JugadorMapper {

    public Jugador toEntity(JugadorRequest r) {
        Jugador e = new Jugador();
        e.setDocumento(r.documento());
        e.setTipoDocumento(r.tipoDocumento());
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setFechaNacimiento(r.fechaNacimiento());
        e.setEstatura(r.estatura());
        e.setPeso(r.peso());
        e.setPiernaHabil(r.piernaHabil());
        e.setNacionalidad(r.nacionalidad());
        e.setEstado(r.estado());
        e.setPosicion(r.posicion());
        e.getCorreos().addAll(r.correos());
        e.getTelefonos().addAll(r.telefonos());
        return e;
    }

    public void actualizarEntidad(Jugador e, JugadorRequest r) {
        e.setDocumento(r.documento());
        e.setTipoDocumento(r.tipoDocumento());
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setFechaNacimiento(r.fechaNacimiento());
        e.setEstatura(r.estatura());
        e.setPeso(r.peso());
        e.setPiernaHabil(r.piernaHabil());
        e.setNacionalidad(r.nacionalidad());
        e.setEstado(r.estado());
        e.setPosicion(r.posicion());
        e.getCorreos().clear();
        e.getCorreos().addAll(r.correos());
        e.getTelefonos().clear();
        e.getTelefonos().addAll(r.telefonos());
    }

    public JugadorResponse toResponse(Jugador e) {
        return new JugadorResponse(
                e.getIdJugador(),
                e.getDocumento(),
                e.getTipoDocumento(),
                e.getPrimerNombre(),
                e.getSegundoNombre(),
                e.getPrimerApellido(),
                e.getSegundoApellido(),
                e.getFechaNacimiento(),
                e.getEstatura(),
                e.getPeso(),
                e.getPiernaHabil(),
                e.getNacionalidad(),
                e.getEstado(),
                e.getPosicion(),
                new HashSet<>(e.getCorreos()),
                new HashSet<>(e.getTelefonos()));
    }
}
