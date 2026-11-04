package co.edu.unbosque.entity;

import jakarta.persistence.*;

/** Un tiro de la tanda de penales de un partido (orden 1 = primer tiro, del local). */
@Entity
@Table(name = "penal")
public class Penal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penal")
    private Long idPenal;

    @Column(name = "partido_id_partido", nullable = false)
    private Long idPartido;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "equipo_id_equipo", nullable = false)
    private Long idEquipo;

    /** 'S' si fue gol, 'N' si fallo. */
    @Column(name = "convertido", nullable = false, length = 1)
    private String convertido;

    public Penal() {
    }

    public Long getIdPenal() {
        return idPenal;
    }

    public void setIdPenal(Long idPenal) {
        this.idPenal = idPenal;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getConvertido() {
        return convertido;
    }

    public void setConvertido(String convertido) {
        this.convertido = convertido;
    }
}
