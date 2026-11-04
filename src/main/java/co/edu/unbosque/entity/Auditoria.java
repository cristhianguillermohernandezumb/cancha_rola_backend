package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long idAuditoria;

    @Column(name = "detalle", length = 100)
    private String detalle;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "entidad", nullable = false, length = 30)
    private String entidad;

    @Column(name = "id_registro", nullable = false, length = 100)
    private String idRegistro;

    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "torneo_id_torneo")
    private Torneo torneo;

    public Auditoria() {
    }

    public Long getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Long idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
}
