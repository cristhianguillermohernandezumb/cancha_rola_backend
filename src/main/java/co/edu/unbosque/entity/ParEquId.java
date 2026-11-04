package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParEquId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "partido_id_partido", nullable = false)
    private Long idPartido;

    @Column(name = "equ_tor_id_equipo", nullable = false)
    private Long idEquipo;

    @Column(name = "equ_tor_id_torneo", nullable = false)
    private Long idTorneo;

    public ParEquId() {
    }

    public ParEquId(Long idPartido, Long idEquipo, Long idTorneo) {
        this.idPartido = idPartido;
        this.idEquipo = idEquipo;
        this.idTorneo = idTorneo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParEquId)) return false;
        ParEquId other = (ParEquId) o;
        return Objects.equals(idPartido, other.idPartido) && Objects.equals(idEquipo, other.idEquipo) && Objects.equals(idTorneo, other.idTorneo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPartido, idEquipo, idTorneo);
    }
}
