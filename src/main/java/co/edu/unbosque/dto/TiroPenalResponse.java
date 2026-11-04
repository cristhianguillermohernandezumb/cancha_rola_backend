package co.edu.unbosque.dto;

public record TiroPenalResponse(
        Integer orden,
        Long idEquipo,
        String rol,
        Boolean convertido
) {
}
