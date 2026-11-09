package co.edu.unbosque.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.TorUsuResponse;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.entity.TorUsu;
import co.edu.unbosque.entity.TorUsuId;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.TorUsuMapper;
import co.edu.unbosque.repository.TorUsuRepository;

/** Torneos que sigue un usuario (y si quiere recibir notificaciones de ese torneo). */
@Service
public class TorUsuService {

	private final Autorizacion autorizacion;

	private final TorUsuRepository torUsuRepository;
	private final UsuarioService usuarioService;
	private final TorneoService torneoService;
	private final TorUsuMapper torUsuMapper;

	public TorUsuService(TorUsuRepository torUsuRepository, UsuarioService usuarioService,
			TorneoService torneoService, TorUsuMapper torUsuMapper, Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.torUsuRepository = torUsuRepository;
		this.usuarioService = usuarioService;
		this.torneoService = torneoService;
		this.torUsuMapper = torUsuMapper;
	}

	@Transactional
	public TorUsuResponse seguir(Long idUsuario, Long idTorneo, String notificacion) {
		String n = normalizar(notificacion);
		autorizacion.exigirUsuarioPropio(idUsuario);
		Usuario usuario = usuarioService.buscarEntidad(idUsuario);
		Torneo torneo = torneoService.buscarEntidad(idTorneo);
		if (!"ACTIVO".equals(usuario.getEstado())) {
			throw new ReglaNegocioException("El usuario esta INACTIVO y no puede seguir torneos");
		}
		if ("FINALIZADO".equals(torneo.getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo; no se puede seguir");
		}
		TorUsuId id = new TorUsuId(idTorneo, idUsuario);
		if (torUsuRepository.existsById(id)) {
			throw new ReglaNegocioException("El usuario ya sigue este torneo");
		}
		TorUsu seguimiento = new TorUsu();
		seguimiento.setId(id);
		seguimiento.setFechaSeguimiento(LocalDate.now());
		seguimiento.setNotificacion(n);
		TorUsu guardado = torUsuRepository.save(seguimiento);
		guardado.setTorneo(torneo);
		guardado.setUsuario(usuario);
		return torUsuMapper.toResponse(guardado);
	}

	@Transactional(readOnly = true)
	public List<TorUsuResponse> listarPorUsuario(Long idUsuario) {
		autorizacion.exigirUsuarioPropio(idUsuario);
		usuarioService.buscarEntidad(idUsuario);
		return torUsuRepository.buscarPorUsuario(idUsuario).stream().map(torUsuMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<TorUsuResponse> listarPorTorneo(Long idTorneo) {
		torneoService.buscarEntidad(idTorneo);
		return torUsuRepository.buscarPorTorneo(idTorneo).stream().map(torUsuMapper::toResponse).toList();
	}

	@Transactional
	public TorUsuResponse cambiarNotificacion(Long idUsuario, Long idTorneo, String valor) {
		TorUsu seguimiento = buscarEntidad(idUsuario, idTorneo);
		autorizacion.exigirUsuarioPropio(idUsuario);
		seguimiento.setNotificacion(normalizar(valor));
		return torUsuMapper.toResponse(torUsuRepository.save(seguimiento));
	}

	@Transactional
	public void dejarDeSeguir(Long idUsuario, Long idTorneo) {
		TorUsu seguimiento = buscarEntidad(idUsuario, idTorneo);
		autorizacion.exigirUsuarioPropio(idUsuario);
		torUsuRepository.deleteById(seguimiento.getId());
	}

	@Transactional(readOnly = true)
	public TorUsu buscarEntidad(Long idUsuario, Long idTorneo) {
		return torUsuRepository.findById(new TorUsuId(idTorneo, idUsuario)).orElseThrow(() -> new NoEncontradoException(
				"El usuario " + idUsuario + " no sigue el torneo " + idTorneo));
	}

	private String normalizar(String notificacion) {
		String n = notificacion == null ? "" : notificacion.toUpperCase();
		if (!n.equals("S") && !n.equals("N")) {
			throw new ReglaNegocioException("notificacion debe ser S (si) o N (no)");
		}
		return n;
	}

}
