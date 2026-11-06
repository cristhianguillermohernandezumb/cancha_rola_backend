package co.edu.unbosque.repository;

import java.util.List;

import co.edu.unbosque.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

	List<Grupo> findByTorneo_IdTorneo(Long idTorneo);

	boolean existsByTorneo_IdTorneoAndFaseAndLetra(Long idTorneo, Integer fase, String letra);

}
