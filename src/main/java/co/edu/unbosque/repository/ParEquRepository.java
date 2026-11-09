package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.ParEqu;
import co.edu.unbosque.entity.ParEquId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParEquRepository extends JpaRepository<ParEqu, ParEquId> {

	@Query("select p from ParEqu p where p.id.idPartido = :idPartido")
	List<ParEqu> buscarPorPartido(@Param("idPartido") Long idPartido);

	@Query("select count(p) from ParEqu p where p.id.idPartido = :idPartido")
	long contarPorPartido(@Param("idPartido") Long idPartido);

	@Query("select count(p) > 0 from ParEqu p where p.id.idPartido = :idPartido and p.rol = :rol")
	boolean existeRolEnPartido(@Param("idPartido") Long idPartido, @Param("rol") String rol);

	@Query("select count(p) > 0 from ParEqu p where p.id.idEquipo = :idEquipo and p.id.idPartido <> :idPartido and p.partido.fechaHora = :fechaHora")
	boolean existeEquipoEnFecha(@Param("idEquipo") Long idEquipo, @Param("idPartido") Long idPartido,
			@Param("fechaHora") java.time.LocalDateTime fechaHora);

	@Query("select p from ParEqu p where p.id.idTorneo = :idTorneo and p.partido.estado = 'FINALIZADO'")
	List<ParEqu> buscarDePartidosFinalizados(@Param("idTorneo") Long idTorneo);

}
