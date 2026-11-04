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
        // El servicio debe cifrar la contrasenia (BCrypt) y poner fechaRegistro y estado antes de guardar.
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

    /** Copia los datos editables del perfil sobre una entidad existente (no toca id, rol, contrasenia, fechaRegistro ni estado). */
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
