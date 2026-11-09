package co.edu.unbosque.repository;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.unbosque.entity.Alineacion;
import co.edu.unbosque.entity.AlineacionId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlineacionRepository extends JpaRepository<Alineacion, AlineacionId> {

	@Query("select a from Alineacion a where a.id.idPartido = :idPartido")
	List<Alineacion> buscarPorPartido(@Param("idPartido") Long idPartido);

	@Query("select a from Alineacion a where a.id.idPartido = :idPartido and a.id.idEquipo = :idEquipo")
	List<Alineacion> buscarPorPartidoYEquipo(@Param("idPartido") Long idPartido, @Param("idEquipo") Long idEquipo);

	@Query("select count(a) > 0 from Alineacion a where a.id.idPartido = :idPartido and a.id.idJugador = :idJugador")
	boolean existeJugadorEnPartido(@Param("idPartido") Long idPartido, @Param("idJugador") Long idJugador);

	@Query("select count(a) > 0 from Alineacion a where a.id.idJugador = :idJugador and a.id.idTorneo = :idTorneo")
	boolean existeJugadorEnTorneo(@Param("idJugador") Long idJugador, @Param("idTorneo") Long idTorneo);

	@Query("select count(a) from Alineacion a where a.id.idPartido = :idPartido and a.id.idEquipo = :idEquipo and a.titular = 'S'")
	long contarTitulares(@Param("idPartido") Long idPartido, @Param("idEquipo") Long idEquipo);

	@Query("select p.idPartido from Partido p where p.idPartido <> :idPartido and p.fechaHora <= :fecha "
			+ "and exists (select 1 from Alineacion a where a.id.idPartido = p.idPartido and a.id.idEquipo = :idEquipo) "
			+ "order by p.fechaHora desc, p.idPartido desc")
	List<Long> ultimosPartidosConAlineacion(@Param("idEquipo") Long idEquipo, @Param("idPartido") Long idPartido,
			@Param("fecha") LocalDateTime fecha, Pageable pageable);

}
