package co.edu.unbosque.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.UsuarioActualizarRequest;
import co.edu.unbosque.dto.UsuarioRequest;
import co.edu.unbosque.dto.UsuarioResponse;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.event.UsuarioRegistradoEvent;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.UsuarioMapper;
import co.edu.unbosque.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final Autorizacion autorizacion;

	private static final String ACTIVO = "ACTIVO";
	private static final String INACTIVO = "INACTIVO";
	private static final List<String> ROLES = List.of("ADMIN", "ORGANIZADOR", "ESPECTADOR");

	private final UsuarioRepository usuarioRepository;
	private final UsuarioMapper usuarioMapper;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventos;

	public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
			PasswordEncoder passwordEncoder, Autorizacion autorizacion, ApplicationEventPublisher eventos) {
		this.eventos = eventos;
		this.autorizacion = autorizacion;
		this.usuarioRepository = usuarioRepository;
		this.usuarioMapper = usuarioMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UsuarioResponse registrar(UsuarioRequest request) {
		if (usuarioRepository.existsByCorreo(request.correo())) {
			throw new ReglaNegocioException("Ya existe un usuario con el correo " + request.correo());
		}
		Usuario usuario = usuarioMapper.toEntity(request);
		usuario.setContrasenia(passwordEncoder.encode(request.contrasenia()));
		usuario.setFechaRegistro(LocalDate.now());
		usuario.setEstado(ACTIVO);
		Usuario guardado = usuarioRepository.save(usuario);
		if ("ESPECTADOR".equals(guardado.getRol())) {
			eventos.publishEvent(new UsuarioRegistradoEvent(guardado.getIdUsuario(), guardado.getPrimerNombre(),
					guardado.getCorreo()));
		}
		return usuarioMapper.toResponse(guardado);
	}

	@Transactional(readOnly = true)
	public List<UsuarioResponse> listar() {
		return usuarioRepository.findAll().stream().map(usuarioMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public UsuarioResponse obtener(Long idUsuario) {
		autorizacion.exigirUsuarioPropio(idUsuario);
		return usuarioMapper.toResponse(buscarEntidad(idUsuario));
	}

	@Transactional
	public UsuarioResponse actualizar(Long idUsuario, UsuarioActualizarRequest request) {
		autorizacion.exigirUsuarioPropio(idUsuario);
		Usuario usuario = buscarEntidad(idUsuario);
		if (!usuario.getCorreo().equalsIgnoreCase(request.correo()) && usuarioRepository.existsByCorreo(request.correo())) {
			throw new ReglaNegocioException("Ya existe un usuario con el correo " + request.correo());
		}
		usuarioMapper.actualizarEntidad(usuario, request);
		if (request.contrasenia() != null && !request.contrasenia().isBlank()) {
			usuario.setContrasenia(passwordEncoder.encode(request.contrasenia()));
		}
		return usuarioMapper.toResponse(usuarioRepository.save(usuario));
	}

	@Transactional
	public UsuarioResponse cambiarRol(Long idUsuario, String rol) {
		if (!ROLES.contains(rol)) {
			throw new ReglaNegocioException("Rol no valido. Valores permitidos: ADMIN, ORGANIZADOR, ESPECTADOR");
		}
		exigirOtroUsuario(idUsuario, "No puede cambiar su propio rol");
		Usuario usuario = buscarEntidad(idUsuario);
		usuario.setRol(rol);
		return usuarioMapper.toResponse(usuarioRepository.save(usuario));
	}

	@Transactional
	public UsuarioResponse desactivar(Long idUsuario) {
		exigirOtroUsuario(idUsuario, "No puede desactivar su propia cuenta");
		return cambiarEstado(idUsuario, INACTIVO);
	}

	@Transactional
	public UsuarioResponse activar(Long idUsuario) {
		return cambiarEstado(idUsuario, ACTIVO);
	}

	@Transactional(readOnly = true)
	public Usuario buscarEntidad(Long idUsuario) {
		return usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new NoEncontradoException("No existe el usuario con id " + idUsuario));
	}

	private void exigirOtroUsuario(Long idUsuario, String mensaje) {
		autorizacion.actual().ifPresent(u -> {
			if (u.idUsuario().equals(idUsuario)) {
				throw new ReglaNegocioException(mensaje);
			}
		});
	}

	private UsuarioResponse cambiarEstado(Long idUsuario, String estado) {
		Usuario usuario = buscarEntidad(idUsuario);
		usuario.setEstado(estado);
		return usuarioMapper.toResponse(usuarioRepository.save(usuario));
	}

}
