package co.edu.unbosque.dto;

public record EquGruResponse(
        Long idGrupo,
        String letraGrupo,
        Long idTorneo,
        Long idEquipo,
        String nombreEquipo
) {
}
