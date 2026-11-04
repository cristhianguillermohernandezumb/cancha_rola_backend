package co.edu.unbosque.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TorneoResponse(
        Long idTorneo,
        String nombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado,
        Long telefono,
        String modalidad,
        String categoria,
        BigDecimal costoInscripcion,
        Integer maxEquipos,
        Long idOrganizador,
        String nombreOrganizador
) {
}
