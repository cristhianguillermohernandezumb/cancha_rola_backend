package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.EquGru;
import co.edu.unbosque.entity.EquGruId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquGruRepository extends JpaRepository<EquGru, EquGruId> {

	@Query("select e from EquGru e where e.id.idGrupo = :idGrupo")
	List<EquGru> buscarPorGrupo(@Param("idGrupo") Long idGrupo);

	@Query("select e from EquGru e where e.id.idTorneo = :idTorneo")
	List<EquGru> buscarPorTorneo(@Param("idTorneo") Long idTorneo);

}
