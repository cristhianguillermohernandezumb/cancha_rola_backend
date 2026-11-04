package co.edu.unbosque.dto;

public record ParArbResponse(
        Long idPartido,
        Long idArbitro,
        String nombreArbitro,
        String rol
) {
}
