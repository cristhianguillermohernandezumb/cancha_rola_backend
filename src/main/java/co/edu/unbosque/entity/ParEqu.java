package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "par_equ")
public class ParEqu {

    @EmbeddedId
    private ParEquId id;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partido_id_partido", referencedColumnName = "id_partido", insertable = false, updatable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "equ_tor_id_equipo", referencedColumnName = "equipo_id_equipo", insertable = false, updatable = false),
            @JoinColumn(name = "equ_tor_id_torneo", referencedColumnName = "torneo_id_torneo", insertable = false, updatable = false)
    })
    private EquTor equTor;

    public ParEqu() {
    }

    public ParEquId getId() {
        return id;
    }

    public void setId(ParEquId id) {
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

    public EquTor getEquTor() {
        return equTor;
    }

    public void setEquTor(EquTor equTor) {
        this.equTor = equTor;
    }
}
