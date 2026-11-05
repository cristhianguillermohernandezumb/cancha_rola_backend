package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.GrupoRequest;
import co.edu.unbosque.dto.GrupoResponse;
import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.GrupoMapper;
import co.edu.unbosque.repository.EquGruRepository;
import co.edu.unbosque.repository.GrupoRepository;

@Service
public class GrupoService {

	private final Autorizacion autorizacion;

	private final GrupoRepository grupoRepository;
	private final EquGruRepository equGruRepository;
	private final GrupoMapper grupoMapper;
	private final TorneoService torneoService;

	public GrupoService(GrupoRepository grupoRepository, EquGruRepository equGruRepository, GrupoMapper grupoMapper,
			TorneoService torneoService, Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.grupoRepository = grupoRepository;
		this.equGruRepository = equGruRepository;
		this.grupoMapper = grupoMapper;
		this.torneoService = torneoService;
	}

	@Transactional
	public GrupoResponse crear(GrupoRequest request) {
		Torneo torneo = torneoService.buscarEntidad(request.idTorneo());
		autorizacion.exigirDuenoDeTorneo(torneo);
		validarTorneoAbierto(torneo);
		validarUnico(request.idTorneo(), request.fase(), request.letra());
		Grupo grupo = grupoMapper.toEntity(request, torneo);
		return grupoMapper.toResponse(grupoRepository.save(grupo));
	}

	@Transactional(readOnly = true)
	public List<GrupoResponse> listarPorTorneo(Long idTorneo) {
		torneoService.buscarEntidad(idTorneo);
		return grupoRepository.findByTorneo_IdTorneo(idTorneo).stream().map(grupoMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public GrupoResponse obtener(Long idGrupo) {
		return grupoMapper.toResponse(buscarEntidad(idGrupo));
	}

	@Transactional
	public GrupoResponse actualizar(Long idGrupo, GrupoRequest request) {
		Grupo grupo = buscarEntidad(idGrupo);
		autorizacion.exigirDuenoDeTorneo(grupo.getTorneo());
		if (!grupo.getTorneo().getIdTorneo().equals(request.idTorneo())) {
			throw new ReglaNegocioException("No se puede cambiar el torneo de un grupo");
		}
		validarTorneoAbierto(grupo.getTorneo());
		boolean cambio = !grupo.getFase().equals(request.fase()) || !grupo.getLetra().equals(request.letra());
		if (cambio) {
			validarUnico(request.idTorneo(), request.fase(), request.letra());
		}
		grupoMapper.actualizarEntidad(grupo, request);
		return grupoMapper.toResponse(grupoRepository.save(grupo));
	}

	/** Solo se borra un grupo vacio (sin equipos asignados) de un torneo que no ha finalizado. */
	@Transactional
	public void eliminar(Long idGrupo) {
		Grupo grupo = buscarEntidad(idGrupo);
		autorizacion.exigirDuenoDeTorneo(grupo.getTorneo());
		validarTorneoAbierto(grupo.getTorneo());
		if (!equGruRepository.buscarPorGrupo(idGrupo).isEmpty()) {
			throw new ReglaNegocioException("El grupo tiene equipos asignados; quitalos primero");
		}
		grupoRepository.deleteById(idGrupo);
	}

	/** Lo usan otros servicios (EquGruService, partidos...) para obtener la entidad completa. */
	@Transactional(readOnly = true)
	public Grupo buscarEntidad(Long idGrupo) {
		return grupoRepository.findById(idGrupo)
				.orElseThrow(() -> new NoEncontradoException("No existe el grupo con id " + idGrupo));
	}

	private void validarTorneoAbierto(Torneo torneo) {
		if ("FINALIZADO".equals(torneo.getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo; no se pueden modificar sus grupos");
		}
	}

	/** Regla UNIQUE de la base: no puede haber dos grupos con la misma fase y letra en un torneo. */
	private void validarUnico(Long idTorneo, Integer fase, String letra) {
		if (grupoRepository.existsByTorneo_IdTorneoAndFaseAndLetra(idTorneo, fase, letra)) {
			throw new ReglaNegocioException("Ya existe el grupo " + letra + " de la fase " + fase + " en este torneo");
		}
	}

}
