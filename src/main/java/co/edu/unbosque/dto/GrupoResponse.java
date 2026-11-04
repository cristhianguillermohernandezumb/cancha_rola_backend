package co.edu.unbosque.dto;

public record GrupoResponse(
        Long idGrupo,
        Integer fase,
        String letra,
        Long idTorneo
) {
}
