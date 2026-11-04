package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EquTorId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "equipo_id_equipo", nullable = false)
    private Long idEquipo;

    @Column(name = "torneo_id_torneo", nullable = false)
    private Long idTorneo;

    public EquTorId() {
    }

    public EquTorId(Long idEquipo, Long idTorneo) {
        this.idEquipo = idEquipo;
        this.idTorneo = idTorneo;
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
        if (!(o instanceof EquTorId)) return false;
        EquTorId other = (EquTorId) o;
        return Objects.equals(idEquipo, other.idEquipo) && Objects.equals(idTorneo, other.idTorneo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, idTorneo);
    }
}
