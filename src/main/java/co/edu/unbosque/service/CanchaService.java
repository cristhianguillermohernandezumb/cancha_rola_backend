package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.CanchaRequest;
import co.edu.unbosque.dto.CanchaResponse;
import co.edu.unbosque.entity.Cancha;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.mapper.CanchaMapper;
import co.edu.unbosque.repository.CanchaRepository;
import co.edu.unbosque.repository.PartidoRepository;

@Service
public class CanchaService {

	private final CanchaRepository canchaRepository;
	private final PartidoRepository partidoRepository;
	private final CanchaMapper canchaMapper;

	public CanchaService(CanchaRepository canchaRepository, PartidoRepository partidoRepository,
			CanchaMapper canchaMapper) {
		this.canchaRepository = canchaRepository;
		this.partidoRepository = partidoRepository;
		this.canchaMapper = canchaMapper;
	}

	@Transactional
	public CanchaResponse crear(CanchaRequest request) {
		validarNoDuplicada(request);
		Cancha cancha = canchaMapper.toEntity(request);
		return canchaMapper.toResponse(canchaRepository.save(cancha));
	}

	/** Filtros opcionales: primero localidad y, si no viene, tipo de superficie. Sin filtros lista todas. */
	@Transactional(readOnly = true)
	public List<CanchaResponse> listar(String localidad, String tipoSuperficie) {
		List<Cancha> canchas;
		if (localidad != null && !localidad.isBlank()) {
			canchas = canchaRepository.findByLocalidadIgnoreCase(localidad);
		} else if (tipoSuperficie != null && !tipoSuperficie.isBlank()) {
			canchas = canchaRepository.findByTipoSuperficie(tipoSuperficie.toUpperCase());
		} else {
			canchas = canchaRepository.findAll();
		}
		return canchas.stream().map(canchaMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public CanchaResponse obtener(Long idCancha) {
		return canchaMapper.toResponse(buscarEntidad(idCancha));
	}

	@Transactional
	public CanchaResponse actualizar(Long idCancha, CanchaRequest request) {
		Cancha cancha = buscarEntidad(idCancha);
		boolean cambio = !cancha.getNombre().equalsIgnoreCase(request.nombre())
				|| !cancha.getDireccion().equalsIgnoreCase(request.direccion());
		if (cambio) {
			validarNoDuplicada(request);
		}
		canchaMapper.actualizarEntidad(cancha, request);
		return canchaMapper.toResponse(canchaRepository.save(cancha));
	}

	/** Solo se borra una cancha que no tenga partidos programados o jugados. */
	@Transactional
	public void eliminar(Long idCancha) {
		buscarEntidad(idCancha);
		if (partidoRepository.existsByCancha_IdCancha(idCancha)) {
			throw new ReglaNegocioException("La cancha tiene partidos asociados y no se puede eliminar");
		}
		canchaRepository.deleteById(idCancha);
	}

	/** Lo usan otros servicios (partidos) para obtener la entidad completa. */
	@Transactional(readOnly = true)
	public Cancha buscarEntidad(Long idCancha) {
		return canchaRepository.findById(idCancha)
				.orElseThrow(() -> new NoEncontradoException("No existe la cancha con id " + idCancha));
	}

	private void validarNoDuplicada(CanchaRequest request) {
		if (canchaRepository.existsByNombreIgnoreCaseAndDireccionIgnoreCase(request.nombre(), request.direccion())) {
			throw new ReglaNegocioException(
					"Ya existe una cancha llamada " + request.nombre() + " en " + request.direccion());
		}
	}

}
