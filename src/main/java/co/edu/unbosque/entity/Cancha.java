package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cancha")
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancha")
    private Long idCancha;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 50)
    private String direccion;

    @Column(name = "tipo_superficie", nullable = false, length = 50)
    private String tipoSuperficie;

    @Column(name = "localidad", nullable = false, length = 50)
    private String localidad;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "largo", nullable = false)
    private Integer largo;

    @Column(name = "ancho", nullable = false)
    private Integer ancho;

    public Cancha() {
    }

    public Long getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(Long idCancha) {
        this.idCancha = idCancha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoSuperficie() {
        return tipoSuperficie;
    }

    public void setTipoSuperficie(String tipoSuperficie) {
        this.tipoSuperficie = tipoSuperficie;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getLargo() {
        return largo;
    }

    public void setLargo(Integer largo) {
        this.largo = largo;
    }

    public Integer getAncho() {
        return ancho;
    }

    public void setAncho(Integer ancho) {
        this.ancho = ancho;
    }
}
