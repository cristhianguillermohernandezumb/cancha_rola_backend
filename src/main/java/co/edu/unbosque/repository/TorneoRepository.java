package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {

	List<Torneo> findByEstado(String estado);

	List<Torneo> findByOrganizador_IdUsuario(Long idUsuario);

	boolean existsByNombreIgnoreCase(String nombre);

	boolean existsByNombreIgnoreCaseAndOrganizador_IdUsuario(String nombre, Long idUsuario);

}
