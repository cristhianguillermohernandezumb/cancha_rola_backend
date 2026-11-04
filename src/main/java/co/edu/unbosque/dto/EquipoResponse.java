package co.edu.unbosque.dto;

import java.time.LocalDate;

public record EquipoResponse(
        Long idEquipo,
        String nombre,
        LocalDate fechaFundacion,
        String correo,
        Long telefonoEquipo,
        byte[] escudo
) {
}
