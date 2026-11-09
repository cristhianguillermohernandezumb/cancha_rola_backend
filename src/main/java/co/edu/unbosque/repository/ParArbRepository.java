package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.ParArb;
import co.edu.unbosque.entity.ParArbId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParArbRepository extends JpaRepository<ParArb, ParArbId> {

	@Query("select p from ParArb p where p.id.idPartido = :idPartido")
	List<ParArb> buscarPorPartido(@Param("idPartido") Long idPartido);

	@Query("select p from ParArb p where p.id.idArbitro = :idArbitro")
	List<ParArb> buscarPorArbitro(@Param("idArbitro") Long idArbitro);

	@Query("select count(p) > 0 from ParArb p where p.id.idPartido = :idPartido and p.rol = :rol")
	boolean existeRolEnPartido(@Param("idPartido") Long idPartido, @Param("rol") String rol);

	@Query("select count(p) > 0 from ParArb p where p.id.idArbitro = :idArbitro and p.id.idPartido <> :idPartido and p.partido.fechaHora = :fechaHora")
	boolean existeArbitroEnFecha(@Param("idArbitro") Long idArbitro, @Param("idPartido") Long idPartido,
			@Param("fechaHora") java.time.LocalDateTime fechaHora);

}
