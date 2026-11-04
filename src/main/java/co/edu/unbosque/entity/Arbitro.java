package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "arbitro")
public class Arbitro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arbitro")
    private Long idArbitro;

    @Column(name = "documento", nullable = false, length = 50)
    private String documento;

    @Column(name = "tipo_documento", nullable = false, length = 15)
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

    @Column(name = "numero_licencia", nullable = false)
    private Integer numeroLicencia;

    @Column(name = "nacionalidad", nullable = false, length = 50)
    private String nacionalidad;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "correo_arbitro", joinColumns = @JoinColumn(name = "arbitro_id_arbitro"))
    @Column(name = "correo_arbitro", nullable = false)
    private Set<String> correos = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "telefono_arbitro", joinColumns = @JoinColumn(name = "arbitro_id_arbitro"))
    @Column(name = "telefono_arbitro", nullable = false)
    private Set<String> telefonos = new HashSet<>();

    public Arbitro() {
    }

    public Long getIdArbitro() {
        return idArbitro;
    }

    public void setIdArbitro(Long idArbitro) {
        this.idArbitro = idArbitro;
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

    public Integer getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(Integer numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
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
