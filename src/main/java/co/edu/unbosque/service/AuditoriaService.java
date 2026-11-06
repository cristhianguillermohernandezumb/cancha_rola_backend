package co.edu.unbosque.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.AuditoriaResponse;
import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.mapper.AuditoriaMapper;
import co.edu.unbosque.repository.AuditoriaRepository;
import co.edu.unbosque.repository.TorneoRepository;
import co.edu.unbosque.repository.UsuarioRepository;

/** Guarda y consulta el historial de cambios. La auditoria no se edita ni se borra desde la API. */
@Service
public class AuditoriaService {

	private final AuditoriaRepository auditoriaRepository;
	private final UsuarioRepository usuarioRepository;
	private final TorneoRepository torneoRepository;
	private final AuditoriaMapper auditoriaMapper;

	public AuditoriaService(AuditoriaRepository auditoriaRepository, UsuarioRepository usuarioRepository,
			TorneoRepository torneoRepository, AuditoriaMapper auditoriaMapper) {
		this.auditoriaRepository = auditoriaRepository;
		this.usuarioRepository = usuarioRepository;
		this.torneoRepository = torneoRepository;
		this.auditoriaMapper = auditoriaMapper;
	}

	
	@Transactional
	public boolean registrar(Long idUsuario, String entidad, String idRegistro, String accion, String detalle,
			Long idTorneo) {
		Usuario usuario = idUsuario == null ? null : usuarioRepository.findById(idUsuario).orElse(null);
		if (usuario == null) {
			return false;
		}
		Auditoria a = new Auditoria();
		a.setUsuario(usuario);
		a.setFechaHora(LocalDateTime.now());
		a.setEntidad(recortar(entidad, 30));
		a.setIdRegistro(recortar(idRegistro, 100));
		a.setAccion(accion);
		a.setDetalle(recortar(detalle, 100));
		a.setTorneo(idTorneo == null ? null : torneoRepository.findById(idTorneo).orElse(null));
		auditoriaRepository.save(a);
		return true;
	}

	@Transactional(readOnly = true)
	public List<AuditoriaResponse> listar(Long idUsuario, Long idTorneo) {
		List<Auditoria> registros;
		if (idUsuario != null) {
			registros = auditoriaRepository.findByUsuario_IdUsuarioOrderByFechaHoraDesc(idUsuario);
		} else if (idTorneo != null) {
			registros = auditoriaRepository.findByTorneo_IdTorneoOrderByFechaHoraDesc(idTorneo);
		} else {
			registros = auditoriaRepository.findAllByOrderByFechaHoraDesc();
		}
		return registros.stream().map(auditoriaMapper::toResponse).toList();
	}

	private String recortar(String texto, int max) {
		if (texto == null) {
			return null;
		}
		return texto.length() <= max ? texto : texto.substring(0, max);
	}

}
