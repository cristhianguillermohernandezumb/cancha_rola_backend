package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.EquTor;
import co.edu.unbosque.entity.EquTorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquTorRepository extends JpaRepository<EquTor, EquTorId> {

	@Query("select e from EquTor e where e.id.idTorneo = :idTorneo")
	List<EquTor> buscarPorTorneo(@Param("idTorneo") Long idTorneo);

	@Query("select e from EquTor e where e.id.idEquipo = :idEquipo")
	List<EquTor> buscarPorEquipo(@Param("idEquipo") Long idEquipo);

	/** Para validar el maximo de equipos (max_equipos) de un torneo. */
	@Query("select count(e) from EquTor e where e.id.idTorneo = :idTorneo")
	long contarPorTorneo(@Param("idTorneo") Long idTorneo);

}
