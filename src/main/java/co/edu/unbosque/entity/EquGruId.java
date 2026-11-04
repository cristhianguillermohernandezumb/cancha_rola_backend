package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EquGruId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "grupo_id_grupo", nullable = false)
    private Long idGrupo;

    @Column(name = "grupo_id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "equ_tor_equipo_id_equipo", nullable = false)
    private Long idEquipo;

    public EquGruId() {
    }

    public EquGruId(Long idGrupo, Long idTorneo, Long idEquipo) {
        this.idGrupo = idGrupo;
        this.idTorneo = idTorneo;
        this.idEquipo = idEquipo;
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquGruId)) return false;
        EquGruId other = (EquGruId) o;
        return Objects.equals(idGrupo, other.idGrupo) && Objects.equals(idTorneo, other.idTorneo) && Objects.equals(idEquipo, other.idEquipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGrupo, idTorneo, idEquipo);
    }
}
