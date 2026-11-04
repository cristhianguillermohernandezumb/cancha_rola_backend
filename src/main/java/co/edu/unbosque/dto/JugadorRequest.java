package co.edu.unbosque.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public record JugadorRequest(
        @NotBlank @Size(max = 50) String documento,
        @NotBlank @Pattern(regexp = "CC|CE|TI|PAS", message = "debe ser CC, CE, TI o PAS") String tipoDocumento,
        @NotBlank @Size(max = 50) String primerNombre,
        @NotNull @Size(max = 50) String segundoNombre,
        @NotBlank @Size(max = 50) String primerApellido,
        @NotNull @Size(max = 50) String segundoApellido,
        @NotNull @Past LocalDate fechaNacimiento,
        @NotNull @Positive Integer estatura,
        @NotNull @Positive Double peso,
        @NotBlank @Pattern(regexp = "DERECHA|IZQUIERDA|AMBIDIESTRO", message = "valores permitidos: DERECHA, IZQUIERDA, AMBIDIESTRO") String piernaHabil,
        @NotBlank @Size(max = 50) String nacionalidad,
        @NotBlank @Pattern(regexp = "ACTIVO|LESIONADO|SUSPENDIDO", message = "valores permitidos: ACTIVO, LESIONADO, SUSPENDIDO") String estado,
        @NotBlank @Pattern(regexp = "PORTERO|DEFENSA|VOLANTE|DELANTERO", message = "valores permitidos: PORTERO, DEFENSA, VOLANTE, DELANTERO") String posicion,
        @NotNull Set<@Email @Size(max = 100) String> correos,
        @NotNull Set<@Pattern(regexp = "\\d{7,20}", message = "solo digitos (7 a 20)") String> telefonos
) {
}
