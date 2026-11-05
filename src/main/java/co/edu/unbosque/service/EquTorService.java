package co.edu.unbosque.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.EquTorResponse;
import co.edu.unbosque.entity.EquTor;
import co.edu.unbosque.entity.EquTorId;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.entity.Torneo;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.security.Autorizacion;
import co.edu.unbosque.mapper.EquTorMapper;
import co.edu.unbosque.repository.EquTorRepository;
import co.edu.unbosque.repository.InscripcionJugadorRepository;

/** Inscripcion de un equipo en un torneo (tabla equ_tor). */
@Service
public class EquTorService {

	private final Autorizacion autorizacion;

	private static final String SIN_PAGAR = "SIN_PAGAR";
	private static final String PAGADA = "PAGADA";

	private final EquTorRepository equTorRepository;
	private final InscripcionJugadorRepository inscripcionJugadorRepository;
	private final EquTorMapper equTorMapper;
	private final EquipoService equipoService;
	private final TorneoService torneoService;

	public EquTorService(EquTorRepository equTorRepository, InscripcionJugadorRepository inscripcionJugadorRepository,
			EquTorMapper equTorMapper, EquipoService equipoService, TorneoService torneoService, Autorizacion autorizacion) {
		this.autorizacion = autorizacion;
		this.equTorRepository = equTorRepository;
		this.inscripcionJugadorRepository = inscripcionJugadorRepository;
		this.equTorMapper = equTorMapper;
		this.equipoService = equipoService;
		this.torneoService = torneoService;
	}

	@Transactional
	public EquTorResponse inscribir(Long idTorneo, Long idEquipo) {
		Torneo torneo = torneoService.buscarEntidad(idTorneo);
		autorizacion.exigirDuenoDeTorneo(torneo);
		Equipo equipo = equipoService.buscarEntidad(idEquipo);
		if (!"INSCRIPCION".equals(torneo.getEstado())) {
			throw new ReglaNegocioException("El torneo no esta en etapa de inscripcion (estado: " + torneo.getEstado() + ")");
		}
		EquTorId id = new EquTorId(idEquipo, idTorneo);
		if (equTorRepository.existsById(id)) {
			throw new ReglaNegocioException("El equipo ya esta inscrito en este torneo");
		}
		long inscritos = equTorRepository.contarPorTorneo(idTorneo);
		if (inscritos >= torneo.getMaxEquipos()) {
			throw new ReglaNegocioException("El torneo ya alcanzo el maximo de " + torneo.getMaxEquipos() + " equipos");
		}
		EquTor inscripcion = new EquTor();
		inscripcion.setId(id);
		inscripcion.setEstadoInscripcion(SIN_PAGAR);
		inscripcion.setFechaInscripcion(LocalDate.now());
		EquTor guardada = equTorRepository.save(inscripcion);
		// Las relaciones son de solo lectura: se asignan para que la respuesta lleve los nombres.
		guardada.setEquipo(equipo);
		guardada.setTorneo(torneo);
		return equTorMapper.toResponse(guardada);
	}

	@Transactional(readOnly = true)
	public List<EquTorResponse> listarPorTorneo(Long idTorneo) {
		torneoService.buscarEntidad(idTorneo);
		return equTorRepository.buscarPorTorneo(idTorneo).stream().map(equTorMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<EquTorResponse> listarPorEquipo(Long idEquipo) {
		equipoService.buscarEntidad(idEquipo);
		return equTorRepository.buscarPorEquipo(idEquipo).stream().map(equTorMapper::toResponse).toList();
	}

	@Transactional
	public EquTorResponse marcarPagada(Long idTorneo, Long idEquipo) {
		EquTor inscripcion = buscarEntidad(idTorneo, idEquipo);
		autorizacion.exigirDuenoDeTorneo(inscripcion.getTorneo());
		if ("FINALIZADO".equals(inscripcion.getTorneo().getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo");
		}
		if (PAGADA.equals(inscripcion.getEstadoInscripcion())) {
			throw new ReglaNegocioException("La inscripcion ya esta pagada");
		}
		inscripcion.setEstadoInscripcion(PAGADA);
		return equTorMapper.toResponse(equTorRepository.save(inscripcion));
	}

	/** Retira un equipo del torneo. Solo en etapa de inscripcion y si no tiene jugadores inscritos. */
	@Transactional
	public void retirar(Long idTorneo, Long idEquipo) {
		EquTor inscripcion = buscarEntidad(idTorneo, idEquipo);
		autorizacion.exigirDuenoDeTorneo(inscripcion.getTorneo());
		if (!"INSCRIPCION".equals(inscripcion.getTorneo().getEstado())) {
			throw new ReglaNegocioException("Solo se puede retirar un equipo mientras el torneo esta en inscripcion");
		}
		if (!inscripcionJugadorRepository.buscarPorEquipoYTorneo(idEquipo, idTorneo).isEmpty()) {
			throw new ReglaNegocioException("El equipo tiene jugadores inscritos en este torneo; retiralos primero");
		}
		equTorRepository.deleteById(inscripcion.getId());
	}

	@Transactional(readOnly = true)
	public EquTor buscarEntidad(Long idTorneo, Long idEquipo) {
		return equTorRepository.findById(new EquTorId(idEquipo, idTorneo)).orElseThrow(() -> new NoEncontradoException(
				"El equipo " + idEquipo + " no esta inscrito en el torneo " + idTorneo));
	}

}
