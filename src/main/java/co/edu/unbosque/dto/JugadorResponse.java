package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.util.Set;

public record JugadorResponse(
        Long idJugador,
        String documento,
        String tipoDocumento,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        LocalDate fechaNacimiento,
        Integer estatura,
        Double peso,
        String piernaHabil,
        String nacionalidad,
        String estado,
        String posicion,
        Set<String> correos,
        Set<String> telefonos
) {
}
