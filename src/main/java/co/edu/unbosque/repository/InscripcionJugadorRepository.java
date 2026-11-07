package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.InscripcionJugador;
import co.edu.unbosque.entity.InscripcionJugadorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InscripcionJugadorRepository extends JpaRepository<InscripcionJugador, InscripcionJugadorId> {

	@Query("select i from InscripcionJugador i where i.id.idEquipo = :idEquipo and i.id.idTorneo = :idTorneo")
	List<InscripcionJugador> buscarPorEquipoYTorneo(@Param("idEquipo") Long idEquipo, @Param("idTorneo") Long idTorneo);

	@Query("select i from InscripcionJugador i where i.id.idJugador = :idJugador")
	List<InscripcionJugador> buscarPorJugador(@Param("idJugador") Long idJugador);

	/** Regla UNIQUE de la base: un jugador solo puede estar en un equipo por torneo. */
	@Query("select count(i) > 0 from InscripcionJugador i where i.id.idJugador = :idJugador and i.id.idTorneo = :idTorneo")
	boolean existeJugadorEnTorneo(@Param("idJugador") Long idJugador, @Param("idTorneo") Long idTorneo);

	/** Regla UNIQUE de la base: el numero de camiseta no se repite en el equipo del torneo. */
	@Query("select count(i) > 0 from InscripcionJugador i where i.id.idEquipo = :idEquipo and i.id.idTorneo = :idTorneo and i.numeroCamiseta = :numero")
	boolean existeCamiseta(@Param("idEquipo") Long idEquipo, @Param("idTorneo") Long idTorneo, @Param("numero") Integer numero);

}
