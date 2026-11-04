package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record ParArbRequest(
        @NotNull Long idPartido,
        @NotNull Long idArbitro,
        @NotBlank @Pattern(regexp = "CENTRAL|ASISTENTE_1|ASISTENTE_2|CUARTO|VAR", message = "valores permitidos: CENTRAL, ASISTENTE_1, ASISTENTE_2, CUARTO, VAR") String rol
) {
}
