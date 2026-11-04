package co.edu.unbosque.repository;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {

	boolean existsByTipoDocumentoAndDocumento(String tipoDocumento, String documento);

	Optional<Entrenador> findByTipoDocumentoAndDocumento(String tipoDocumento, String documento);

	List<Entrenador> findByEquipo_IdEquipo(Long idEquipo);

}
