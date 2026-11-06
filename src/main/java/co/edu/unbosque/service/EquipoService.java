package co.edu.unbosque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.dto.EquipoRequest;
import co.edu.unbosque.dto.EquipoResponse;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.exception.NoEncontradoException;
import co.edu.unbosque.exception.ReglaNegocioException;
import co.edu.unbosque.mapper.EquipoMapper;
import co.edu.unbosque.repository.EquipoRepository;

@Service
public class EquipoService {

	private static final int MAX_ESCUDO_BYTES = 2 * 1024 * 1024;

	private final EquipoRepository equipoRepository;
	private final EquipoMapper equipoMapper;

	public EquipoService(EquipoRepository equipoRepository, EquipoMapper equipoMapper) {
		this.equipoRepository = equipoRepository;
		this.equipoMapper = equipoMapper;
	}

	@Transactional
	public EquipoResponse crear(EquipoRequest request) {
		if (equipoRepository.existsByNombreIgnoreCase(request.nombre())) {
			throw new ReglaNegocioException("Ya existe un equipo llamado " + request.nombre());
		}
		Equipo equipo = equipoMapper.toEntity(request);
		return equipoMapper.toResponse(equipoRepository.save(equipo));
	}

	@Transactional(readOnly = true)
	public List<EquipoResponse> listar(String nombre) {
		List<Equipo> equipos = (nombre == null || nombre.isBlank()) ? equipoRepository.findAll()
				: equipoRepository.findByNombreContainingIgnoreCase(nombre);
		return equipos.stream().map(equipoMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public EquipoResponse obtener(Long idEquipo) {
		return equipoMapper.toResponse(buscarEntidad(idEquipo));
	}

	@Transactional
	public EquipoResponse actualizar(Long idEquipo, EquipoRequest request) {
		Equipo equipo = buscarEntidad(idEquipo);
		boolean cambioNombre = !equipo.getNombre().equalsIgnoreCase(request.nombre());
		if (cambioNombre && equipoRepository.existsByNombreIgnoreCase(request.nombre())) {
			throw new ReglaNegocioException("Ya existe un equipo llamado " + request.nombre());
		}
		equipoMapper.actualizarEntidad(equipo, request);
		return equipoMapper.toResponse(equipoRepository.save(equipo));
	}

	@Transactional(readOnly = true)
	public byte[] obtenerEscudo(Long idEquipo) {
		return buscarEntidad(idEquipo).getEscudo();
	}

	@Transactional
	public void actualizarEscudo(Long idEquipo, byte[] escudo) {
		if (escudo == null || escudo.length == 0) {
			throw new ReglaNegocioException("Debe enviar una imagen");
		}
		if (escudo.length > MAX_ESCUDO_BYTES) {
			throw new ReglaNegocioException("El escudo no puede superar los 2 MB");
		}
		if (tipoDeImagen(escudo) == null) {
			throw new ReglaNegocioException("El escudo debe ser una imagen PNG, JPG, GIF o WEBP");
		}
		Equipo equipo = buscarEntidad(idEquipo);
		equipo.setEscudo(escudo);
		equipoRepository.save(equipo);
	}

	public static String tipoDeImagen(byte[] b) {
		if (b == null || b.length < 12) {
			return null;
		}
		if ((b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G') {
			return "image/png";
		}
		if ((b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) {
			return "image/jpeg";
		}
		if (b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8') {
			return "image/gif";
		}
		if (b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F' && b[8] == 'W' && b[9] == 'E' && b[10] == 'B'
				&& b[11] == 'P') {
			return "image/webp";
		}
		return null;
	}

	@Transactional(readOnly = true)
	public Equipo buscarEntidad(Long idEquipo) {
		return equipoRepository.findById(idEquipo)
				.orElseThrow(() -> new NoEncontradoException("No existe el equipo con id " + idEquipo));
	}

}
