package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

	List<Equipo> findByNombreContainingIgnoreCase(String nombre);

	boolean existsByNombreIgnoreCase(String nombre);

}
