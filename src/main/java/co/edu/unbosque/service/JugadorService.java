package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.JugadorRequest;
import co.edu.unbosque.dto.JugadorResponse;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.mapper.JugadorMapper;
import co.edu.unbosque.repository.JugadorRepository;

@Service
public class JugadorService {

	private static final List<String> ESTADOS = List.of("ACTIVO", "LESIONADO", "SUSPENDIDO");

	private final JugadorRepository jugadorRepository;
	private final JugadorMapper jugadorMapper;

	public JugadorService(JugadorRepository jugadorRepository, JugadorMapper jugadorMapper) {
		this.jugadorRepository = jugadorRepository;
		this.jugadorMapper = jugadorMapper;
	}

	@Transactional
	public JugadorResponse crear(JugadorRequest request) {
		validarDocumentoLibre(request.tipoDocumento(), request.documento());
		Jugador jugador = jugadorMapper.toEntity(request);
		return jugadorMapper.toResponse(jugadorRepository.save(jugador));
	}

	@Transactional(readOnly = true)
	public List<JugadorResponse> listar(String estado, String posicion) {
		List<Jugador> jugadores;
		if (estado != null && !estado.isBlank()) {
			jugadores = jugadorRepository.findByEstado(estado.toUpperCase());
		} else if (posicion != null && !posicion.isBlank()) {
			jugadores = jugadorRepository.findByPosicion(posicion.toUpperCase());
		} else {
			jugadores = jugadorRepository.findAll();
		}
		return jugadores.stream().map(jugadorMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public JugadorResponse obtener(Long idJugador) {
		return jugadorMapper.toResponse(buscarEntidad(idJugador));
	}

	@Transactional(readOnly = true)
	public JugadorResponse obtenerPorDocumento(String tipoDocumento, String documento) {
		Jugador jugador = jugadorRepository.findByTipoDocumentoAndDocumento(tipoDocumento.toUpperCase(), documento)
				.orElseThrow(() -> new NoEncontradoException(
						"No existe un jugador con documento " + tipoDocumento.toUpperCase() + " " + documento));
		return jugadorMapper.toResponse(jugador);
	}

	@Transactional
	public JugadorResponse actualizar(Long idJugador, JugadorRequest request) {
		Jugador jugador = buscarEntidad(idJugador);
		boolean cambioDocumento = !jugador.getTipoDocumento().equals(request.tipoDocumento())
				|| !jugador.getDocumento().equals(request.documento());
		if (cambioDocumento) {
			validarDocumentoLibre(request.tipoDocumento(), request.documento());
		}
		jugadorMapper.actualizarEntidad(jugador, request);
		return jugadorMapper.toResponse(jugadorRepository.save(jugador));
	}

	@Transactional
	public JugadorResponse cambiarEstado(Long idJugador, String nuevoEstado) {
		String estado = nuevoEstado == null ? "" : nuevoEstado.toUpperCase();
		if (!ESTADOS.contains(estado)) {
			throw new ReglaNegocioException("Estado invalido. Valores permitidos: " + String.join(", ", ESTADOS));
		}
		Jugador jugador = buscarEntidad(idJugador);
		jugador.setEstado(estado);
		return jugadorMapper.toResponse(jugadorRepository.save(jugador));
	}

	@Transactional(readOnly = true)
	public Jugador buscarEntidad(Long idJugador) {
		return jugadorRepository.findById(idJugador)
				.orElseThrow(() -> new NoEncontradoException("No existe el jugador con id " + idJugador));
	}

	private void validarDocumentoLibre(String tipoDocumento, String documento) {
		if (jugadorRepository.existsByTipoDocumentoAndDocumento(tipoDocumento, documento)) {
			throw new ReglaNegocioException("Ya existe un jugador con documento " + tipoDocumento + " " + documento);
		}
	}

}
