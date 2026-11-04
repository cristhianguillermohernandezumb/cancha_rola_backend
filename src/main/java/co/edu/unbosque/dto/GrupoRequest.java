package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;

public record GrupoRequest(
        @NotNull @Positive Integer fase,
        @NotBlank @Pattern(regexp = "[A-Z]", message = "debe ser una letra mayuscula A-Z") String letra,
        @NotNull Long idTorneo
) {
}
