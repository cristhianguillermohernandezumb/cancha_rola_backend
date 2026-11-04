package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

	List<Auditoria> findAllByOrderByFechaHoraDesc();

	List<Auditoria> findByUsuario_IdUsuarioOrderByFechaHoraDesc(Long idUsuario);

	List<Auditoria> findByTorneo_IdTorneoOrderByFechaHoraDesc(Long idTorneo);

}
