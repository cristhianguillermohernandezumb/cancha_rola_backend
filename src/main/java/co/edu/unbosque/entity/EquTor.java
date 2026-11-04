package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "equ_tor")
public class EquTor {

    @EmbeddedId
    private EquTorId id;

    @Column(name = "estado_inscripcion", nullable = false, length = 20)
    private String estadoInscripcion;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo_id_equipo", referencedColumnName = "id_equipo", insertable = false, updatable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "torneo_id_torneo", referencedColumnName = "id_torneo", insertable = false, updatable = false)
    private Torneo torneo;

    public EquTor() {
    }

    public EquTorId getId() {
        return id;
    }

    public void setId(EquTorId id) {
        this.id = id;
    }

    public String getEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(String estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
}
