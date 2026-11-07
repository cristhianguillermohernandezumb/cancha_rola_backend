package co.edu.unbosque.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import co.edu.unbosque.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/** Origenes del frontend permitidos (CORS). Se cambia con app.cors.origins=http://...,http://... */
	@Value("${app.cors.origins:http://localhost:4200,http://localhost:3000,http://localhost:5173}")
	private String origenesPermitidos;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService,
			UsuarioRepository usuarioRepository) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(b -> b.disable()).formLogin(f -> f.disable())
				.exceptionHandling(e -> e.authenticationEntryPoint((request, response, ex) -> escribir(response, 401,
						"Debe iniciar sesion: envie el encabezado Authorization: Bearer <token>"))
						.accessDeniedHandler((request, response, ex) -> escribir(response, 403,
								"No tiene permiso para esta operacion")))
				.authorizeHttpRequests(a -> a
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
						.requestMatchers("/auth/me").authenticated()
						.requestMatchers("/auditoria/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PATCH, "/usuarios/*/activar", "/usuarios/*/desactivar",
							"/usuarios/*/rol")
						.hasRole("ADMIN")
						.requestMatchers("/usuarios/*/seguimientos/**").authenticated()
						.requestMatchers(HttpMethod.GET, "/usuarios/*").authenticated()
						.requestMatchers(HttpMethod.PUT, "/usuarios/*").authenticated()
						.requestMatchers(HttpMethod.GET, "/torneos/*/seguidores")
						.hasAnyRole("ORGANIZADOR", "ADMIN")
						.requestMatchers(HttpMethod.GET, "/jugadores/**", "/arbitros/**", "/entrenadores/**")
						.hasAnyRole("ORGANIZADOR", "ADMIN")
						.requestMatchers(HttpMethod.GET, "/**").permitAll()
						.anyRequest().hasAnyRole("ORGANIZADOR", "ADMIN"))
				.addFilterBefore(new JwtAuthFilter(jwtService, usuarioRepository),
						UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	/** Spring Boot crearia un usuario "user" con clave aleatoria; no se usa (el acceso es solo por JWT). */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			throw new UsernameNotFoundException("Autenticacion solo por JWT");
		};
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.stream(origenesPermitidos.split(",")).map(String::trim)
				.filter(s -> !s.isEmpty()).toList());
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
		fuente.registerCorsConfiguration("/**", config);
		return fuente;
	}

	/** Mismo formato JSON que GlobalExceptionHandler, para los errores 401 y 403 que Spring Security resuelve antes. */
	private void escribir(jakarta.servlet.http.HttpServletResponse response, int estado, String mensaje)
			throws java.io.IOException {
		response.setStatus(estado);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"fecha\":\"" + java.time.LocalDateTime.now() + "\",\"estado\":" + estado
				+ ",\"mensaje\":\"" + mensaje + "\"}");
	}

}
