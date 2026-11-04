package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TorUsuId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "torneo_id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "usuario_id_usuario", nullable = false)
    private Long idUsuario;

    public TorUsuId() {
    }

    public TorUsuId(Long idTorneo, Long idUsuario) {
        this.idTorneo = idTorneo;
        this.idUsuario = idUsuario;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TorUsuId)) return false;
        TorUsuId other = (TorUsuId) o;
        return Objects.equals(idTorneo, other.idTorneo) && Objects.equals(idUsuario, other.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTorneo, idUsuario);
    }
}
