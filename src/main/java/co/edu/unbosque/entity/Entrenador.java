package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "entrenador")
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrenador")
    private Long idEntrenador;

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

    @Column(name = "anios_experiencia", nullable = false)
    private Integer aniosExperiencia;

    @Column(name = "nacionalidad", nullable = false, length = 50)
    private String nacionalidad;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "equipo_id_equipo", nullable = false)
    private Equipo equipo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "correo_entrenador", joinColumns = @JoinColumn(name = "entrenador_id_entrenador"))
    @Column(name = "correo_entrenador", nullable = false)
    private Set<String> correos = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "telefono_entrenador", joinColumns = @JoinColumn(name = "entrenador_id_entrenador"))
    @Column(name = "telefono_entrenador", nullable = false)
    private Set<String> telefonos = new HashSet<>();

    public Entrenador() {
    }

    public Long getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(Long idEntrenador) {
        this.idEntrenador = idEntrenador;
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

    public Integer getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(Integer aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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
