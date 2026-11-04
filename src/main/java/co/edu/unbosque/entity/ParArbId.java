package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParArbId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "partido_id_partido", nullable = false)
    private Long idPartido;

    @Column(name = "arbitro_id_arbitro", nullable = false)
    private Long idArbitro;

    public ParArbId() {
    }

    public ParArbId(Long idPartido, Long idArbitro) {
        this.idPartido = idPartido;
        this.idArbitro = idArbitro;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public Long getIdArbitro() {
        return idArbitro;
    }

    public void setIdArbitro(Long idArbitro) {
        this.idArbitro = idArbitro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParArbId)) return false;
        ParArbId other = (ParArbId) o;
        return Objects.equals(idPartido, other.idPartido) && Objects.equals(idArbitro, other.idArbitro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPartido, idArbitro);
    }
}
