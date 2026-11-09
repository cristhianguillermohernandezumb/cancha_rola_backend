package co.edu.unbosque.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

/**
 * Envia correos HTML por SMTP (Gmail). Si el correo no esta configurado (spring.mail.host y
 * spring.mail.username) no hace nada: el resto de la aplicacion funciona igual.
 */
@Service
public class CorreoService {

	private static final Logger log = LoggerFactory.getLogger(CorreoService.class);

	private final ObjectProvider<JavaMailSender> mailSender;
	private final TemplateEngine templateEngine;
	private final String remitente;

	public CorreoService(ObjectProvider<JavaMailSender> mailSender, TemplateEngine templateEngine,
			@Value("${spring.mail.username:}") String remitente) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
		this.remitente = remitente;
	}

	public void enviarBienvenida(String nombre, String correo) {
		enviar(correo, "Bienvenido a Futbol Bogota", "bienvenida", Map.of("nombre", nombre));
	}

	public void enviar(String correo, String asunto, String plantilla, Map<String, Object> variables) {
		JavaMailSender sender = mailSender.getIfAvailable();
		if (sender == null || remitente == null || remitente.isBlank()) {
			log.warn("Correo no configurado (spring.mail.*): no se envia '{}' a {}", asunto, correo);
			return;
		}
		try {
			Context contexto = new Context();
			contexto.setVariables(variables);
			String html = templateEngine.process(plantilla, contexto);

			MimeMessage mensaje = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, "UTF-8");
			helper.setFrom(remitente);
			helper.setTo(correo);
			helper.setSubject(asunto);
			helper.setText(html, true);
			sender.send(mensaje);
			log.info("Correo '{}' enviado a {}", asunto, correo);
		} catch (Exception e) {
			log.warn("No se pudo enviar el correo '{}' a {}: {}", asunto, correo, e.getMessage());
		}
	}

}
