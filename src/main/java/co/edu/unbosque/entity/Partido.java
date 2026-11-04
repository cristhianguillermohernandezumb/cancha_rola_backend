package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partido")
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido")
    private Long idPartido;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "goles_local", nullable = false)
    private Integer golesLocal = 0;

    @Column(name = "goles_visitante", nullable = false)
    private Integer golesVisitante = 0;

    @Column(name = "fase", nullable = false, length = 50)
    private String fase;

    @Column(name = "jornada", nullable = false)
    private Integer jornada;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cancha_id_cancha", nullable = false)
    private Cancha cancha;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "torneo_id_torneo", nullable = false)
    private Torneo torneo;

    public Partido() {
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Integer getJornada() {
        return jornada;
    }

    public void setJornada(Integer jornada) {
        this.jornada = jornada;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
}
