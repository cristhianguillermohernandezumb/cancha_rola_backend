package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "evento_partido")
public class EventoPartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    @Column(name = "minuto", nullable = false)
    private Integer minuto;

    @Column(name = "tipo_evento", nullable = false, length = 50)
    private String tipoEvento;

    @Column(name = "partido_id_partido", nullable = false)
    private Long idPartido;

    @Column(name = "alineacion_id_equipo", nullable = false)
    private Long idEquipo;

    @Column(name = "alineacion_id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "alineacion_id_jugador", nullable = false)
    private Long idJugador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partido_id_partido", referencedColumnName = "id_partido", insertable = false, updatable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "partido_id_partido", referencedColumnName = "par_equ_partido_id_partido", insertable = false, updatable = false),
            @JoinColumn(name = "alineacion_id_equipo", referencedColumnName = "par_equ_equ_tor_id_equipo", insertable = false, updatable = false),
            @JoinColumn(name = "alineacion_id_torneo", referencedColumnName = "par_equ_equ_tor_id_torneo", insertable = false, updatable = false),
            @JoinColumn(name = "alineacion_id_jugador", referencedColumnName = "inscripcion_jugador_id_jugador", insertable = false, updatable = false)
    })
    private Alineacion alineacion;

    public EventoPartido() {
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
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

    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Alineacion getAlineacion() {
        return alineacion;
    }

    public void setAlineacion(Alineacion alineacion) {
        this.alineacion = alineacion;
    }
}
