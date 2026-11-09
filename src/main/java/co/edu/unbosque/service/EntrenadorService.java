package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.EntrenadorPublicoResponse;
import co.edu.unbosque.dto.EntrenadorRequest;
import co.edu.unbosque.dto.EntrenadorResponse;
import co.edu.unbosque.entity.Entrenador;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.mapper.EntrenadorMapper;
import co.edu.unbosque.repository.EntrenadorRepository;

@Service
public class EntrenadorService {

	private final EntrenadorRepository entrenadorRepository;
	private final EquipoService equipoService;
	private final EntrenadorMapper entrenadorMapper;
	private final EquTorService equTorService;

	public EntrenadorService(EntrenadorRepository entrenadorRepository, EquipoService equipoService,
			EntrenadorMapper entrenadorMapper, EquTorService equTorService) {
		this.entrenadorRepository = entrenadorRepository;
		this.equipoService = equipoService;
		this.entrenadorMapper = entrenadorMapper;
		this.equTorService = equTorService;
	}

	@Transactional
	public EntrenadorResponse crear(EntrenadorRequest request) {
		validarDocumentoLibre(request.tipoDocumento(), request.documento());
		Equipo equipo = equipoService.buscarEntidad(request.idEquipo());
		Entrenador entrenador = entrenadorMapper.toEntity(request, equipo);
		return entrenadorMapper.toResponse(entrenadorRepository.save(entrenador));
	}

	@Transactional(readOnly = true)
	public List<EntrenadorResponse> listar(Long idEquipo) {
		List<Entrenador> entrenadores = idEquipo == null ? entrenadorRepository.findAll()
				: entrenadorRepository.findByEquipo_IdEquipo(idEquipo);
		return entrenadores.stream().map(entrenadorMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<EntrenadorPublicoResponse> listarPublico(Long idTorneo, Long idEquipo) {
		equTorService.buscarEntidad(idTorneo, idEquipo);
		return entrenadorRepository.findByEquipo_IdEquipo(idEquipo).stream().map(entrenadorMapper::toPublico)
				.toList();
	}

	@Transactional(readOnly = true)
	public EntrenadorResponse obtener(Long idEntrenador) {
		return entrenadorMapper.toResponse(buscarEntidad(idEntrenador));
	}

	@Transactional(readOnly = true)
	public EntrenadorResponse obtenerPorDocumento(String tipoDocumento, String documento) {
		Entrenador entrenador = entrenadorRepository
				.findByTipoDocumentoAndDocumento(tipoDocumento.toUpperCase(), documento)
				.orElseThrow(() -> new NoEncontradoException(
						"No existe un entrenador con documento " + tipoDocumento.toUpperCase() + " " + documento));
		return entrenadorMapper.toResponse(entrenador);
	}

	@Transactional
	public EntrenadorResponse actualizar(Long idEntrenador, EntrenadorRequest request) {
		Entrenador entrenador = buscarEntidad(idEntrenador);
		boolean cambioDocumento = !entrenador.getTipoDocumento().equals(request.tipoDocumento())
				|| !entrenador.getDocumento().equals(request.documento());
		if (cambioDocumento) {
			validarDocumentoLibre(request.tipoDocumento(), request.documento());
		}
		Equipo equipo = equipoService.buscarEntidad(request.idEquipo());
		entrenadorMapper.actualizarEntidad(entrenador, request, equipo);
		return entrenadorMapper.toResponse(entrenadorRepository.save(entrenador));
	}

	@Transactional
	public void eliminar(Long idEntrenador) {
		buscarEntidad(idEntrenador);
		entrenadorRepository.deleteById(idEntrenador);
	}

	@Transactional(readOnly = true)
	public Entrenador buscarEntidad(Long idEntrenador) {
		return entrenadorRepository.findById(idEntrenador)
				.orElseThrow(() -> new NoEncontradoException("No existe el entrenador con id " + idEntrenador));
	}

	private void validarDocumentoLibre(String tipoDocumento, String documento) {
		if (entrenadorRepository.existsByTipoDocumentoAndDocumento(tipoDocumento, documento)) {
			throw new ReglaNegocioException(
					"Ya existe un entrenador con documento " + tipoDocumento + " " + documento);
		}
	}

}
