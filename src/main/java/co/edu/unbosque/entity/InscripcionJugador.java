package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inscripcion_jugador")
public class InscripcionJugador {

    @EmbeddedId
    private InscripcionJugadorId id;

    @Column(name = "numero_camiseta", nullable = false)
    private Integer numeroCamiseta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jugador_id_jugador", referencedColumnName = "id_jugador", insertable = false, updatable = false)
    private Jugador jugador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "equ_tor_equipo_id_equipo", referencedColumnName = "equipo_id_equipo", insertable = false, updatable = false),
            @JoinColumn(name = "equ_tor_id_torneo", referencedColumnName = "torneo_id_torneo", insertable = false, updatable = false)
    })
    private EquTor equTor;

    public InscripcionJugador() {
    }

    public InscripcionJugadorId getId() {
        return id;
    }

    public void setId(InscripcionJugadorId id) {
        this.id = id;
    }

    public Integer getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(Integer numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public EquTor getEquTor() {
        return equTor;
    }

    public void setEquTor(EquTor equTor) {
        this.equTor = equTor;
    }
}
