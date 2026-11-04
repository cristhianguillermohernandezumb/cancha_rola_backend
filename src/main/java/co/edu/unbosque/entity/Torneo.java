package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "torneo")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_torneo")
    private Long idTorneo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "telefono", nullable = false)
    private Long telefono;

    @Column(name = "modalidad", nullable = false, length = 20)
    private String modalidad;

    @Column(name = "categoria", nullable = false, length = 20)
    private String categoria;

    @Column(name = "costo_inscripcion", nullable = false)
    private BigDecimal costoInscripcion;

    @Column(name = "max_equipos", nullable = false)
    private Integer maxEquipos;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id_usuario", nullable = false)
    private Usuario organizador;

    public Torneo() {
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getCostoInscripcion() {
        return costoInscripcion;
    }

    public void setCostoInscripcion(BigDecimal costoInscripcion) {
        this.costoInscripcion = costoInscripcion;
    }

    public Integer getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(Integer maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
    }
}
