package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tor_usu")
public class TorUsu {

    @EmbeddedId
    private TorUsuId id;

    @Column(name = "fecha_seguimiento", nullable = false)
    private LocalDate fechaSeguimiento;

    @Column(name = "notificacion", nullable = false, length = 1)
    private String notificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "torneo_id_torneo", referencedColumnName = "id_torneo", insertable = false, updatable = false)
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    public TorUsu() {
    }

    public TorUsuId getId() {
        return id;
    }

    public void setId(TorUsuId id) {
        this.id = id;
    }

    public LocalDate getFechaSeguimiento() {
        return fechaSeguimiento;
    }

    public void setFechaSeguimiento(LocalDate fechaSeguimiento) {
        this.fechaSeguimiento = fechaSeguimiento;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
