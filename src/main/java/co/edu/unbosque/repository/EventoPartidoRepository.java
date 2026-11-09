package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.EventoPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventoPartidoRepository extends JpaRepository<EventoPartido, Long> {

	List<EventoPartido> findByIdPartidoOrderByMinutoAsc(Long idPartido);

	List<EventoPartido> findByIdPartidoAndIdJugador(Long idPartido, Long idJugador);

	@Query("select count(e) from EventoPartido e where e.idPartido = :idPartido and e.idEquipo = :idEquipo and e.tipoEvento = :tipo")
	long contarPorPartidoEquipoYTipo(@Param("idPartido") Long idPartido, @Param("idEquipo") Long idEquipo, @Param("tipo") String tipo);

	@Query("select e.idJugador, e.idEquipo, count(e) from EventoPartido e where e.idTorneo = :idTorneo and e.tipoEvento = 'GOL' group by e.idJugador, e.idEquipo")
	List<Object[]> contarGolesPorJugador(@Param("idTorneo") Long idTorneo);

	@Query("select e.idJugador, e.idEquipo, count(e) from EventoPartido e where e.idTorneo = :idTorneo and e.tipoEvento = 'GOL' "
			+ "and exists (select 1 from Partido p where p.idPartido = e.idPartido and lower(p.fase) = 'grupos') "
			+ "group by e.idJugador, e.idEquipo")
	List<Object[]> contarGolesPorJugadorEnGrupos(@Param("idTorneo") Long idTorneo);

	@Query("select e.idJugador, e.idEquipo, count(e) from EventoPartido e where e.idTorneo = :idTorneo and e.tipoEvento = 'GOL' "
			+ "and exists (select 1 from Partido p where p.idPartido = e.idPartido and lower(p.fase) <> 'grupos') "
			+ "group by e.idJugador, e.idEquipo")
	List<Object[]> contarGolesPorJugadorEnEliminatoria(@Param("idTorneo") Long idTorneo);

}
