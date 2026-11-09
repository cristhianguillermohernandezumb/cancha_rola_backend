package co.edu.unbosque.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import co.edu.unbosque.entity.Usuario;

/**
 * Crea y valida los tokens JWT (firmados con HS256). Propiedades opcionales en application.properties:
 * jwt.secret (minimo 32 caracteres) y jwt.expiracion-minutos (por defecto 480 = 8 horas).
 * Si no hay jwt.secret se genera una clave aleatoria al arrancar: funciona, pero los tokens dejan de valer
 * cada vez que se reinicia el servidor.
 */
@Service
public class JwtService {

	private static final Logger log = LoggerFactory.getLogger(JwtService.class);

	private final JwtEncoder encoder;
	private final JwtDecoder decoder;
	private final long expiracionMinutos;

	public JwtService(@Value("${jwt.secret:}") String secreto,
			@Value("${jwt.expiracion-minutos:480}") long expiracionMinutos) {
		byte[] bytes;
		if (secreto == null || secreto.isBlank()) {
			bytes = new byte[32];
			new SecureRandom().nextBytes(bytes);
			log.warn("jwt.secret no esta definido: se genero una clave temporal. Defina jwt.secret (32+ caracteres).");
		} else {
			bytes = secreto.getBytes(StandardCharsets.UTF_8);
			if (bytes.length < 32) {
				throw new IllegalStateException("jwt.secret debe tener al menos 32 caracteres");
			}
		}
		SecretKey clave = new SecretKeySpec(bytes, "HmacSHA256");
		this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(clave));
		this.decoder = NimbusJwtDecoder.withSecretKey(clave).macAlgorithm(MacAlgorithm.HS256).build();
		this.expiracionMinutos = expiracionMinutos;
	}

	public String generar(Usuario usuario) {
		Instant ahora = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder().subject(String.valueOf(usuario.getIdUsuario()))
				.issuedAt(ahora).expiresAt(ahora.plus(expiracionMinutos, ChronoUnit.MINUTES))
				.claim("correo", usuario.getCorreo()).claim("rol", usuario.getRol()).build();
		JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
		return encoder.encode(JwtEncoderParameters.from(cabecera, claims)).getTokenValue();
	}

	public Optional<Long> validar(String token) {
		try {
			Jwt jwt = decoder.decode(token);
			return Optional.of(Long.valueOf(jwt.getSubject()));
		} catch (RuntimeException e) {
			return Optional.empty();
		}
	}

	public long getExpiracionMinutos() {
		return expiracionMinutos;
	}

}
