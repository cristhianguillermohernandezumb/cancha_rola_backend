package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "par_arb")
public class ParArb {

    @EmbeddedId
    private ParArbId id;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partido_id_partido", referencedColumnName = "id_partido", insertable = false, updatable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arbitro_id_arbitro", referencedColumnName = "id_arbitro", insertable = false, updatable = false)
    private Arbitro arbitro;

    public ParArb() {
    }

    public ParArbId getId() {
        return id;
    }

    public void setId(ParArbId id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }
}
