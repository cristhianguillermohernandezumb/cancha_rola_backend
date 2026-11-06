package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.InscripcionJugadorRequest;
import co.edu.unbosque.dto.InscripcionJugadorResponse;
import co.edu.unbosque.entity.EquTor;
import co.edu.unbosque.entity.InscripcionJugador;
import co.edu.unbosque.entity.InscripcionJugadorId;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.mapper.InscripcionJugadorMapper;
import co.edu.unbosque.repository.AlineacionRepository;
import co.edu.unbosque.repository.InscripcionJugadorRepository;
import co.edu.unbosque.security.Autorizacion;

/** Inscripcion de un jugador en un equipo dentro de un torneo (tabla inscripcion_jugador). */
@Service
public class InscripcionJugadorService {

	private final InscripcionJugadorRepository inscripcionRepository;
	private final AlineacionRepository alineacionRepository;
	private final InscripcionJugadorMapper inscripcionMapper;
	private final JugadorService jugadorService;
	private final EquTorService equTorService;
	private final Autorizacion autorizacion;

	public InscripcionJugadorService(InscripcionJugadorRepository inscripcionRepository,
			AlineacionRepository alineacionRepository, InscripcionJugadorMapper inscripcionMapper,
			JugadorService jugadorService, EquTorService equTorService, Autorizacion autorizacion) {
		this.inscripcionRepository = inscripcionRepository;
		this.alineacionRepository = alineacionRepository;
		this.inscripcionMapper = inscripcionMapper;
		this.jugadorService = jugadorService;
		this.equTorService = equTorService;
		this.autorizacion = autorizacion;
	}

	@Transactional
	public InscripcionJugadorResponse inscribir(InscripcionJugadorRequest request) {
		Jugador jugador = jugadorService.buscarEntidad(request.idJugador());
		EquTor equTor = equTorService.buscarEntidad(request.idTorneo(), request.idEquipo());
		autorizacion.exigirDuenoDeTorneo(equTor.getTorneo());
		validarTorneoAbierto(equTor);
		if (inscripcionRepository.existeJugadorEnTorneo(request.idJugador(), request.idTorneo())) {
			throw new ReglaNegocioException("El jugador ya esta inscrito en un equipo de este torneo");
		}
		validarCamisetaLibre(request.idEquipo(), request.idTorneo(), request.numeroCamiseta());
		InscripcionJugador inscripcion = inscripcionMapper.toEntity(request);
		InscripcionJugador guardada = inscripcionRepository.save(inscripcion);
		guardada.setJugador(jugador);
		guardada.setEquTor(equTor);
		return inscripcionMapper.toResponse(guardada);
	}

	@Transactional(readOnly = true)
	public List<InscripcionJugadorResponse> listarPorEquipoYTorneo(Long idTorneo, Long idEquipo) {
		equTorService.buscarEntidad(idTorneo, idEquipo);
		return inscripcionRepository.buscarPorEquipoYTorneo(idEquipo, idTorneo).stream()
				.map(inscripcionMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<InscripcionJugadorResponse> listarPorJugador(Long idJugador) {
		jugadorService.buscarEntidad(idJugador);
		return inscripcionRepository.buscarPorJugador(idJugador).stream().map(inscripcionMapper::toResponse)
				.toList();
	}

	@Transactional
	public InscripcionJugadorResponse cambiarCamiseta(Long idTorneo, Long idEquipo, Long idJugador, Integer numero) {
		if (numero == null || numero < 0 || numero > 99) {
			throw new ReglaNegocioException("El numero de camiseta debe estar entre 0 y 99");
		}
		InscripcionJugador inscripcion = buscarEntidad(idTorneo, idEquipo, idJugador);
		autorizacion.exigirDuenoDeTorneo(inscripcion.getEquTor().getTorneo());
		validarTorneoAbierto(inscripcion.getEquTor());
		if (!numero.equals(inscripcion.getNumeroCamiseta())) {
			validarCamisetaLibre(idEquipo, idTorneo, numero);
			inscripcion.setNumeroCamiseta(numero);
			inscripcion = inscripcionRepository.save(inscripcion);
		}
		return inscripcionMapper.toResponse(inscripcion);
	}

	@Transactional
	public void retirar(Long idTorneo, Long idEquipo, Long idJugador) {
		InscripcionJugador inscripcion = buscarEntidad(idTorneo, idEquipo, idJugador);
		autorizacion.exigirDuenoDeTorneo(inscripcion.getEquTor().getTorneo());
		validarTorneoAbierto(inscripcion.getEquTor());
		if (alineacionRepository.existeJugadorEnTorneo(idJugador, idTorneo)) {
			throw new ReglaNegocioException("El jugador ya fue alineado en un partido de este torneo");
		}
		inscripcionRepository.deleteById(inscripcion.getId());
	}

	@Transactional(readOnly = true)
	public InscripcionJugador buscarEntidad(Long idTorneo, Long idEquipo, Long idJugador) {
		return inscripcionRepository.findById(new InscripcionJugadorId(idJugador, idEquipo, idTorneo))
				.orElseThrow(() -> new NoEncontradoException("El jugador " + idJugador
						+ " no esta inscrito en el equipo " + idEquipo + " del torneo " + idTorneo));
	}

	private void validarTorneoAbierto(EquTor equTor) {
		if (equTor.getTorneo() != null && "FINALIZADO".equals(equTor.getTorneo().getEstado())) {
			throw new ReglaNegocioException("El torneo ya finalizo; no se pueden modificar sus jugadores");
		}
	}

	private void validarCamisetaLibre(Long idEquipo, Long idTorneo, Integer numero) {
		if (inscripcionRepository.existeCamiseta(idEquipo, idTorneo, numero)) {
			throw new ReglaNegocioException("El numero de camiseta " + numero + " ya esta en uso en este equipo");
		}
	}

}
