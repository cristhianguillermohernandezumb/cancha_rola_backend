package co.edu.unbosque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.entity.Cruce;

public interface CruceRepository extends JpaRepository<Cruce, Long> {

	List<Cruce> findByIdTorneoOrderByRondaAscOrdenAsc(Long idTorneo);

	List<Cruce> findByIdTorneoAndRondaOrderByOrdenAsc(Long idTorneo, String ronda);

}
