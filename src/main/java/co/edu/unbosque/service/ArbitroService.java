package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.ArbitroRequest;
import co.edu.unbosque.dto.ArbitroResponse;
import co.edu.unbosque.entity.Arbitro;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.mapper.ArbitroMapper;
import co.edu.unbosque.repository.ArbitroRepository;

@Service
public class ArbitroService {

	private static final List<String> ESTADOS = List.of("ACTIVO", "INACTIVO");

	private final ArbitroRepository arbitroRepository;
	private final ArbitroMapper arbitroMapper;

	public ArbitroService(ArbitroRepository arbitroRepository, ArbitroMapper arbitroMapper) {
		this.arbitroRepository = arbitroRepository;
		this.arbitroMapper = arbitroMapper;
	}

	@Transactional
	public ArbitroResponse crear(ArbitroRequest request) {
		validarDocumentoLibre(request.tipoDocumento(), request.documento());
		validarLicenciaLibre(request.numeroLicencia());
		Arbitro arbitro = arbitroMapper.toEntity(request);
		return arbitroMapper.toResponse(arbitroRepository.save(arbitro));
	}

	@Transactional(readOnly = true)
	public List<ArbitroResponse> listar(String estado) {
		List<Arbitro> arbitros = (estado == null || estado.isBlank()) ? arbitroRepository.findAll()
				: arbitroRepository.findByEstado(estado.toUpperCase());
		return arbitros.stream().map(arbitroMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ArbitroResponse obtener(Long idArbitro) {
		return arbitroMapper.toResponse(buscarEntidad(idArbitro));
	}

	@Transactional(readOnly = true)
	public ArbitroResponse obtenerPorDocumento(String tipoDocumento, String documento) {
		Arbitro arbitro = arbitroRepository.findByTipoDocumentoAndDocumento(tipoDocumento.toUpperCase(), documento)
				.orElseThrow(() -> new NoEncontradoException(
						"No existe un arbitro con documento " + tipoDocumento.toUpperCase() + " " + documento));
		return arbitroMapper.toResponse(arbitro);
	}

	@Transactional
	public ArbitroResponse actualizar(Long idArbitro, ArbitroRequest request) {
		Arbitro arbitro = buscarEntidad(idArbitro);
		boolean cambioDocumento = !arbitro.getTipoDocumento().equals(request.tipoDocumento())
				|| !arbitro.getDocumento().equals(request.documento());
		if (cambioDocumento) {
			validarDocumentoLibre(request.tipoDocumento(), request.documento());
		}
		if (!arbitro.getNumeroLicencia().equals(request.numeroLicencia())) {
			validarLicenciaLibre(request.numeroLicencia());
		}
		arbitroMapper.actualizarEntidad(arbitro, request);
		return arbitroMapper.toResponse(arbitroRepository.save(arbitro));
	}

	@Transactional
	public ArbitroResponse cambiarEstado(Long idArbitro, String nuevoEstado) {
		String estado = nuevoEstado == null ? "" : nuevoEstado.toUpperCase();
		if (!ESTADOS.contains(estado)) {
			throw new ReglaNegocioException("Estado invalido. Valores permitidos: " + String.join(", ", ESTADOS));
		}
		Arbitro arbitro = buscarEntidad(idArbitro);
		arbitro.setEstado(estado);
		return arbitroMapper.toResponse(arbitroRepository.save(arbitro));
	}

	@Transactional(readOnly = true)
	public Arbitro buscarEntidad(Long idArbitro) {
		return arbitroRepository.findById(idArbitro)
				.orElseThrow(() -> new NoEncontradoException("No existe el arbitro con id " + idArbitro));
	}

	private void validarDocumentoLibre(String tipoDocumento, String documento) {
		if (arbitroRepository.existsByTipoDocumentoAndDocumento(tipoDocumento, documento)) {
			throw new ReglaNegocioException("Ya existe un arbitro con documento " + tipoDocumento + " " + documento);
		}
	}

	private void validarLicenciaLibre(Integer numeroLicencia) {
		if (arbitroRepository.existsByNumeroLicencia(numeroLicencia)) {
			throw new ReglaNegocioException("Ya existe un arbitro con la licencia " + numeroLicencia);
		}
	}

}
