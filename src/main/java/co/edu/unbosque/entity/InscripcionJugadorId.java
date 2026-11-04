package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InscripcionJugadorId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "jugador_id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "equ_tor_equipo_id_equipo", nullable = false)
    private Long idEquipo;

    @Column(name = "equ_tor_id_torneo", nullable = false)
    private Long idTorneo;

    public InscripcionJugadorId() {
    }

    public InscripcionJugadorId(Long idJugador, Long idEquipo, Long idTorneo) {
        this.idJugador = idJugador;
        this.idEquipo = idEquipo;
        this.idTorneo = idTorneo;
    }

    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InscripcionJugadorId)) return false;
        InscripcionJugadorId other = (InscripcionJugadorId) o;
        return Objects.equals(idJugador, other.idJugador) && Objects.equals(idEquipo, other.idEquipo) && Objects.equals(idTorneo, other.idTorneo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJugador, idEquipo, idTorneo);
    }
}
