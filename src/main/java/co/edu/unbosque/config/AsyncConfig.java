package co.edu.unbosque.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/** Permite que el envio de correos se haga en segundo plano, sin hacer esperar la respuesta de la API. */
@Configuration
@EnableAsync
public class AsyncConfig {

}
