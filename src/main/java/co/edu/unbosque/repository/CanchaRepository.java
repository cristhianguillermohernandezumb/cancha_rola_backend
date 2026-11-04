package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {

	List<Cancha> findByLocalidadIgnoreCase(String localidad);

	List<Cancha> findByTipoSuperficie(String tipoSuperficie);

	boolean existsByNombreIgnoreCaseAndDireccionIgnoreCase(String nombre, String direccion);

}
