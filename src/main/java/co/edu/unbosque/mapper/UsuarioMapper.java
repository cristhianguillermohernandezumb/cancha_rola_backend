package co.edu.unbosque.mapper;

import co.edu.unbosque.dto.UsuarioActualizarRequest;
import co.edu.unbosque.dto.UsuarioRequest;
import co.edu.unbosque.dto.UsuarioResponse;
import co.edu.unbosque.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest r) {
        Usuario e = new Usuario();
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setTelefono(r.telefono());
        e.setCorreo(r.correo());
        e.setContrasenia(r.contrasenia());
        e.setRol(r.rol());
        return e;
    }

    public void actualizarEntidad(Usuario e, UsuarioActualizarRequest r) {
        e.setPrimerNombre(r.primerNombre());
        e.setSegundoNombre(r.segundoNombre());
        e.setPrimerApellido(r.primerApellido());
        e.setSegundoApellido(r.segundoApellido());
        e.setTelefono(r.telefono());
        e.setCorreo(r.correo());
    }

    public UsuarioResponse toResponse(Usuario e) {
        return new UsuarioResponse(
                e.getIdUsuario(),
                e.getPrimerNombre(),
                e.getSegundoNombre(),
                e.getPrimerApellido(),
                e.getSegundoApellido(),
                e.getFechaRegistro(),
                e.getRol(),
                e.getTelefono(),
                e.getCorreo(),
                e.getEstado());
    }
}
