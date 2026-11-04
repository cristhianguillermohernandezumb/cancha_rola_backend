package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "equ_gru")
public class EquGru {

    @EmbeddedId
    private EquGruId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grupo_id_grupo", referencedColumnName = "id_grupo", insertable = false, updatable = false)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "equ_tor_equipo_id_equipo", referencedColumnName = "equipo_id_equipo", insertable = false, updatable = false),
            @JoinColumn(name = "grupo_id_torneo", referencedColumnName = "torneo_id_torneo", insertable = false, updatable = false)
    })
    private EquTor equTor;

    public EquGru() {
    }

    public EquGruId getId() {
        return id;
    }

    public void setId(EquGruId id) {
        this.id = id;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public EquTor getEquTor() {
        return equTor;
    }

    public void setEquTor(EquTor equTor) {
        this.equTor = equTor;
    }
}
