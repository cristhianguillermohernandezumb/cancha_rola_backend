package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.TorUsu;
import co.edu.unbosque.entity.TorUsuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TorUsuRepository extends JpaRepository<TorUsu, TorUsuId> {

	@Query("select t from TorUsu t where t.id.idUsuario = :idUsuario")
	List<TorUsu> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

	@Query("select t from TorUsu t where t.id.idTorneo = :idTorneo")
	List<TorUsu> buscarPorTorneo(@Param("idTorneo") Long idTorneo);

}
