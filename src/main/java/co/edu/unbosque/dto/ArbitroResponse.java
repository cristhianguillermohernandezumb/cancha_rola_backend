package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.util.Set;

public record ArbitroResponse(
        Long idArbitro,
        String documento,
        String tipoDocumento,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        LocalDate fechaNacimiento,
        Integer numeroLicencia,
        String nacionalidad,
        String estado,
        Set<String> correos,
        Set<String> telefonos
) {
}
