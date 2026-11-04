package co.edu.unbosque.repository;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {

	boolean existsByTipoDocumentoAndDocumento(String tipoDocumento, String documento);

	Optional<Jugador> findByTipoDocumentoAndDocumento(String tipoDocumento, String documento);

	List<Jugador> findByEstado(String estado);

	List<Jugador> findByPosicion(String posicion);

}
