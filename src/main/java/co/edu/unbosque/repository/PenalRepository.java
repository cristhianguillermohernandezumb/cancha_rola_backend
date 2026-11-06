package co.edu.unbosque.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unbosque.entity.Penal;

public interface PenalRepository extends JpaRepository<Penal, Long> {

	List<Penal> findByIdPartidoOrderByOrdenAsc(Long idPartido);

	@Query("select p from Penal p where p.idPartido in :ids order by p.idPartido, p.orden")
	List<Penal> buscarDePartidos(@Param("ids") Collection<Long> ids);

}
