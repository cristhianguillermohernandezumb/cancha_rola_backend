package co.edu.unbosque.entity;

import jakarta.persistence.*;

/** Un cruce de eliminatoria: quien juega contra quien en una ronda (por ejemplo 32AVOS) antes de programar el partido. */
@Entity
@Table(name = "cruce")
public class Cruce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cruce")
    private Long idCruce;

    @Column(name = "torneo_id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "ronda", nullable = false, length = 20)
    private String ronda;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "local_id_equipo", nullable = false)
    private Long idLocal;

    @Column(name = "visitante_id_equipo", nullable = false)
    private Long idVisitante;

    public Cruce() {
    }

    public Long getIdCruce() {
        return idCruce;
    }

    public void setIdCruce(Long idCruce) {
        this.idCruce = idCruce;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getRonda() {
        return ronda;
    }

    public void setRonda(String ronda) {
        this.ronda = ronda;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Long idLocal) {
        this.idLocal = idLocal;
    }

    public Long getIdVisitante() {
        return idVisitante;
    }

    public void setIdVisitante(Long idVisitante) {
        this.idVisitante = idVisitante;
    }
}
