package co.edu.unbosque.repository;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.unbosque.entity.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidoRepository extends JpaRepository<Partido, Long> {

	List<Partido> findByTorneo_IdTorneo(Long idTorneo);

	List<Partido> findByTorneo_IdTorneoAndJornada(Long idTorneo, Integer jornada);

	List<Partido> findByTorneo_IdTorneoAndEstado(Long idTorneo, String estado);

	List<Partido> findByFechaHoraBetweenOrderByFechaHoraAsc(LocalDateTime desde, LocalDateTime hasta);

	boolean existsByCancha_IdCanchaAndFechaHora(Long idCancha, LocalDateTime fechaHora);

	boolean existsByCancha_IdCancha(Long idCancha);

}
