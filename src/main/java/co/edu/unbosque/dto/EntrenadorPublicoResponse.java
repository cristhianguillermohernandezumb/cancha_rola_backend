package co.edu.unbosque.dto;

/** Datos del entrenador que se pueden mostrar publicamente (sin documento, correos ni telefonos). */
public record EntrenadorPublicoResponse(
        Long idEntrenador,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        Integer aniosExperiencia,
        String nacionalidad
) {
}
