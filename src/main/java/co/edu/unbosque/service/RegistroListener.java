package co.edu.unbosque.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import co.edu.unbosque.event.UsuarioRegistradoEvent;

/** Reacciona al registro de un usuario: espera a que se guarde (commit) y manda el correo en segundo plano. */
@Component
public class RegistroListener {

	private final CorreoService correoService;

	public RegistroListener(CorreoService correoService) {
		this.correoService = correoService;
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void alRegistrarse(UsuarioRegistradoEvent evento) {
		correoService.enviarBienvenida(evento.nombre(), evento.correo());
	}

}
