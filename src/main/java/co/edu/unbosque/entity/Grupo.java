package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long idGrupo;

    @Column(name = "fase", nullable = false)
    private Integer fase;

    @Column(name = "letra", nullable = false, length = 1)
    private String letra;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "torneo_id_torneo", nullable = false)
    private Torneo torneo;

    public Grupo() {
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getFase() {
        return fase;
    }

    public void setFase(Integer fase) {
        this.fase = fase;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
}
