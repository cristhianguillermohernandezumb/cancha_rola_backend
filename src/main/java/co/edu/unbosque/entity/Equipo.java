package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "equipo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Long idEquipo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "fecha_fundacion", nullable = false)
    private LocalDate fechaFundacion;

    @Column(name = "correo", nullable = false, length = 100)
    private String correo;

    @Column(name = "telefono_equipo", nullable = false)
    private Long telefonoEquipo;

    @Lob
    @Column(name = "escudo", nullable = false)
    private byte[] escudo;

    public Equipo() {
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getTelefonoEquipo() {
        return telefonoEquipo;
    }

    public void setTelefonoEquipo(Long telefonoEquipo) {
        this.telefonoEquipo = telefonoEquipo;
    }

    public byte[] getEscudo() {
        return escudo;
    }

    public void setEscudo(byte[] escudo) {
        this.escudo = escudo;
    }
}
