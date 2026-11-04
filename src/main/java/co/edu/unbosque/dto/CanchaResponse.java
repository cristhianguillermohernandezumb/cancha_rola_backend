package co.edu.unbosque.dto;

public record CanchaResponse(
        Long idCancha,
        String nombre,
        String direccion,
        String tipoSuperficie,
        String localidad,
        Integer capacidad,
        Integer largo,
        Integer ancho
) {
}
