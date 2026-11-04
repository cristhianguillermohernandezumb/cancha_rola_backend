package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.util.Set;

public record EntrenadorResponse(
        Long idEntrenador,
        String documento,
        String tipoDocumento,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        LocalDate fechaNacimiento,
        Integer aniosExperiencia,
        String nacionalidad,
        Long idEquipo,
        String nombreEquipo,
        Set<String> correos,
        Set<String> telefonos
) {
}
