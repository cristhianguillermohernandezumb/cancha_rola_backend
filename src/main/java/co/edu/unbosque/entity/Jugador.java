package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jugador")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jugador")
    private Long idJugador;

    @Column(name = "documento", nullable = false, length = 50)
    private String documento;

    @Column(name = "tipo_documento", nullable = false, length = 50)
    private String tipoDocumento;

    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;

    @Column(name = "segundo_nombre", nullable = false, length = 50)
    private String segundoNombre;

    @Column(name = "primer_apellido", nullable = false, length = 50)
    private String primerApellido;

    @Column(name = "segundo_apellido", nullable = false, length = 50)
    private String segundoApellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "estatura", nullable = false)
    private Integer estatura;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @Column(name = "pierna_habil", nullable = false, length = 15)
    private String piernaHabil;

    @Column(name = "nacionalidad", nullable = false, length = 50)
    private String nacionalidad;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "posicion", nullable = false, length = 50)
    private String posicion;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "correo_jugador", joinColumns = @JoinColumn(name = "jugador_id_jugador"))
    @Column(name = "correo_jugador", nullable = false)
    private Set<String> correos = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "telefono_jugador", joinColumns = @JoinColumn(name = "jugador_id_jugador"))
    @Column(name = "telefono_jugador", nullable = false)
    private Set<String> telefonos = new HashSet<>();

    public Jugador() {
    }

    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getEstatura() {
        return estatura;
    }

    public void setEstatura(Integer estatura) {
        this.estatura = estatura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getPiernaHabil() {
        return piernaHabil;
    }

    public void setPiernaHabil(String piernaHabil) {
        this.piernaHabil = piernaHabil;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Set<String> getCorreos() {
        return correos;
    }

    public void setCorreos(Set<String> correos) {
        this.correos = correos;
    }

    public Set<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(Set<String> telefonos) {
        this.telefonos = telefonos;
    }
}
