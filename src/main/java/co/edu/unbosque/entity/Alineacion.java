package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "alineacion")
public class Alineacion {

    @EmbeddedId
    private AlineacionId id;

    @Column(name = "titular", nullable = false, length = 1)
    private String titular;

    @Column(name = "minutos_jugados", nullable = false)
    private Integer minutosJugados;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "par_equ_partido_id_partido", referencedColumnName = "partido_id_partido", insertable = false, updatable = false),
            @JoinColumn(name = "par_equ_equ_tor_id_equipo", referencedColumnName = "equ_tor_id_equipo", insertable = false, updatable = false),
            @JoinColumn(name = "par_equ_equ_tor_id_torneo", referencedColumnName = "equ_tor_id_torneo", insertable = false, updatable = false)
    })
    private ParEqu parEqu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "inscripcion_jugador_id_jugador", referencedColumnName = "jugador_id_jugador", insertable = false, updatable = false),
            @JoinColumn(name = "par_equ_equ_tor_id_equipo", referencedColumnName = "equ_tor_equipo_id_equipo", insertable = false, updatable = false),
            @JoinColumn(name = "par_equ_equ_tor_id_torneo", referencedColumnName = "equ_tor_id_torneo", insertable = false, updatable = false)
    })
    private InscripcionJugador inscripcionJugador;

    public Alineacion() {
    }

    public AlineacionId getId() {
        return id;
    }

    public void setId(AlineacionId id) {
        this.id = id;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public Integer getMinutosJugados() {
        return minutosJugados;
    }

    public void setMinutosJugados(Integer minutosJugados) {
        this.minutosJugados = minutosJugados;
    }

    public ParEqu getParEqu() {
        return parEqu;
    }

    public void setParEqu(ParEqu parEqu) {
        this.parEqu = parEqu;
    }

    public InscripcionJugador getInscripcionJugador() {
        return inscripcionJugador;
    }

    public void setInscripcionJugador(InscripcionJugador inscripcionJugador) {
        this.inscripcionJugador = inscripcionJugador;
    }
}
