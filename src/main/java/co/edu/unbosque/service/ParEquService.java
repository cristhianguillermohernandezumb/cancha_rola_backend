package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.ParEquResponse;
import co.edu.unbosque.entity.EquTor;
import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.entity.ParEquId;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.ParEquMapper;
import co.edu.unbosque.repository.EquTorRepository;
import co.edu.unbosque.repository.ParEquRepository;

/** Los dos equipos de un partido (LOCAL y VISITANTE). */
@Service
public class ParEquService {

	private final Autorizacion autorizacion;

	private final ParEquRepository parEquRepository;
	private final EquTorRepository equTorRepository;
	private final PartidoService partidoService;
	private final ParEquMapper parEquMapper;

	public ParEquService(ParEquRepository parEquRepository, EquTorRepository equTorRepository,
			PartidoService partidoService, ParEquMapper parEquMapper, Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.parEquRepository = parEquRepository;
		this.equTorRepository = equTorRepository;
		this.partidoService = partidoService;
		this.parEquMapper = parEquMapper;
	}

	@Transactional
	public ParEquResponse asignar(Long idPartido, Long idEquipo, String rol) {
		String rolNormal = rol == null ? "" : rol.toUpperCase();
		if (!rolNormal.equals("LOCAL") && !rolNormal.equals("VISITANTE")) {
			throw new ReglaNegocioException("Rol invalido. Valores permitidos: LOCAL, VISITANTE");
		}
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if (!"PROGRAMADO".equals(partido.getEstado()) && !"APLAZADO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se asignan equipos a partidos PROGRAMADO o APLAZADO");
		}
		Long idTorneo = partido.getTorneo().getIdTorneo();
		// El equipo debe estar inscrito en el MISMO torneo del partido.
		EquTor inscripcion = equTorRepository.findById(new co.edu.unbosque.entity.EquTorId(idEquipo, idTorneo))
				.orElseThrow(() -> new ReglaNegocioException(
						"El equipo " + idEquipo + " no esta inscrito en el torneo " + idTorneo + " de este partido"));
		ParEquId id = new ParEquId(idPartido, idEquipo, idTorneo);
		if (parEquRepository.existsById(id)) {
			throw new ReglaNegocioException("El equipo ya esta asignado a este partido");
		}
		if (parEquRepository.existeRolEnPartido(idPartido, rolNormal)) {
			throw new ReglaNegocioException("El partido ya tiene un equipo " + rolNormal);
		}
		if (parEquRepository.existeEquipoEnFecha(idEquipo, idPartido, partido.getFechaHora())) {
			throw new ReglaNegocioException("El equipo ya tiene otro partido a esa misma fecha y hora");
		}
		ParEqu parEqu = new ParEqu();
		parEqu.setId(id);
		parEqu.setRol(rolNormal);
		ParEqu guardado = parEquRepository.save(parEqu);
		// Las relaciones son de solo lectura: se asignan para que la respuesta lleve el nombre.
		guardado.setPartido(partido);
		guardado.setEquTor(inscripcion);
		return parEquMapper.toResponse(guardado);
	}

	@Transactional(readOnly = true)
	public List<ParEquResponse> listarPorPartido(Long idPartido) {
		partidoService.buscarEntidad(idPartido);
		return parEquRepository.buscarPorPartido(idPartido).stream().map(parEquMapper::toResponse).toList();
	}

	@Transactional
	public void quitar(Long idPartido, Long idEquipo) {
		Partido partido = partidoService.buscarEntidad(idPartido);
		autorizacion.exigirDuenoDeTorneo(partido.getTorneo());
		if (!"PROGRAMADO".equals(partido.getEstado()) && !"APLAZADO".equals(partido.getEstado())) {
			throw new ReglaNegocioException("Solo se quitan equipos de partidos PROGRAMADO o APLAZADO");
		}
		ParEquId id = new ParEquId(idPartido, idEquipo, partido.getTorneo().getIdTorneo());
		if (!parEquRepository.existsById(id)) {
			throw new NoEncontradoException("El equipo " + idEquipo + " no esta asignado al partido " + idPartido);
		}
		parEquRepository.deleteById(id);
	}

}
