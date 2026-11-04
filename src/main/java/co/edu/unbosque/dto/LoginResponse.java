package co.edu.unbosque.dto;

/** tipo siempre es "Bearer": el frontend envia el token como "Authorization: Bearer <token>". */
public record LoginResponse(
        String token,
        String tipo,
        long expiraEnMinutos,
        UsuarioResponse usuario
) {
}
