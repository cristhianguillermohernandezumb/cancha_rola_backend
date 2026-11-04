package co.edu.unbosque.repository;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.entity.Arbitro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {

	boolean existsByTipoDocumentoAndDocumento(String tipoDocumento, String documento);

	Optional<Arbitro> findByTipoDocumentoAndDocumento(String tipoDocumento, String documento);

	List<Arbitro> findByEstado(String estado);

	boolean existsByNumeroLicencia(Integer numeroLicencia);

}
