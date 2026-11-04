package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlineacionId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "par_equ_partido_id_partido", nullable = false)
    private Long idPartido;

    @Column(name = "par_equ_equ_tor_id_equipo", nullable = false)
    private Long idEquipo;

    @Column(name = "par_equ_equ_tor_id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "inscripcion_jugador_id_jugador", nullable = false)
    private Long idJugador;

    public AlineacionId() {
    }

    public AlineacionId(Long idPartido, Long idEquipo, Long idTorneo, Long idJugador) {
        this.idPartido = idPartido;
        this.idEquipo = idEquipo;
        this.idTorneo = idTorneo;
        this.idJugador = idJugador;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlineacionId)) return false;
        AlineacionId other = (AlineacionId) o;
        return Objects.equals(idPartido, other.idPartido) && Objects.equals(idEquipo, other.idEquipo) && Objects.equals(idTorneo, other.idTorneo) && Objects.equals(idJugador, other.idJugador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPartido, idEquipo, idTorneo, idJugador);
    }
}
