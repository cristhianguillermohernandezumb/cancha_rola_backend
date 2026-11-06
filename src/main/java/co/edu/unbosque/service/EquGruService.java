package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.EquGruResponse;
import co.edu.unbosque.entity.EquGru;
import co.edu.unbosque.entity.EquGruId;
import co.edu.unbosque.entity.EquTor;
import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.EquGruMapper;
import co.edu.unbosque.repository.EquGruRepository;

/** Asignacion de equipos a los grupos de un torneo (tabla equ_gru). */
@Service
public class EquGruService {

	private final Autorizacion autorizacion;

	private final EquGruRepository equGruRepository;
	private final EquGruMapper equGruMapper;
	private final GrupoService grupoService;
	private final EquTorService equTorService;

	public EquGruService(EquGruRepository equGruRepository, EquGruMapper equGruMapper, GrupoService grupoService,
			EquTorService equTorService, Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.equGruRepository = equGruRepository;
		this.equGruMapper = equGruMapper;
		this.grupoService = grupoService;
		this.equTorService = equTorService;
	}

	@Transactional
	public EquGruResponse asignar(Long idGrupo, Long idEquipo) {
		Grupo grupo = grupoService.buscarEntidad(idGrupo);
		autorizacion.exigirDuenoDeTorneo(grupo.getTorneo());
		Long idTorneo = grupo.getTorneo().getIdTorneo();
		if ("FINALIZADO".equals(grupo.getTorneo().getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo; no se pueden modificar sus grupos");
		}
		EquTor inscripcion = equTorService.buscarEntidad(idTorneo, idEquipo);
		boolean yaEnLaFase = equGruRepository.buscarPorTorneo(idTorneo).stream()
				.anyMatch(eg -> eg.getId().getIdEquipo().equals(idEquipo) && eg.getGrupo() != null
						&& eg.getGrupo().getFase().equals(grupo.getFase()));
		if (yaEnLaFase) {
			throw new ReglaNegocioException("El equipo ya esta asignado a un grupo de la fase " + grupo.getFase());
		}
		EquGru asignacion = new EquGru();
		asignacion.setId(new EquGruId(idGrupo, idTorneo, idEquipo));
		EquGru guardada = equGruRepository.save(asignacion);
		guardada.setGrupo(grupo);
		guardada.setEquTor(inscripcion);
		return equGruMapper.toResponse(guardada);
	}

	@Transactional(readOnly = true)
	public List<EquGruResponse> listarPorGrupo(Long idGrupo) {
		grupoService.buscarEntidad(idGrupo);
		return equGruRepository.buscarPorGrupo(idGrupo).stream().map(equGruMapper::toResponse).toList();
	}

	@Transactional
	public void quitar(Long idGrupo, Long idEquipo) {
		Grupo grupo = grupoService.buscarEntidad(idGrupo);
		autorizacion.exigirDuenoDeTorneo(grupo.getTorneo());
		if ("FINALIZADO".equals(grupo.getTorneo().getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo; no se pueden modificar sus grupos");
		}
		EquGruId id = new EquGruId(idGrupo, grupo.getTorneo().getIdTorneo(), idEquipo);
		if (!equGruRepository.existsById(id)) {
			throw new NoEncontradoException("El equipo " + idEquipo + " no esta en el grupo " + idGrupo);
		}
		equGruRepository.deleteById(id);
	}

}
