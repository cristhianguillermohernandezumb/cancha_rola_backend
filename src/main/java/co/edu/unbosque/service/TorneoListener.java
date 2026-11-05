package co.edu.unbosque.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import co.edu.unbosque.event.TorneoCambioEstadoEvent;

/** Cuando un torneo inicia o termina, espera al commit y avisa por correo (en segundo plano) a quienes lo siguen. */
@Component
public class TorneoListener {

	private final NotificacionTorneoService notificaciones;

	public TorneoListener(NotificacionTorneoService notificaciones) {
		this.notificaciones = notificaciones;
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void alCambiarEstado(TorneoCambioEstadoEvent evento) {
		if ("EN_CURSO".equals(evento.estado())) {
			notificaciones.notificarInicio(evento.idTorneo());
		} else if ("FINALIZADO".equals(evento.estado())) {
			notificaciones.notificarFinal(evento.idTorneo());
		}
	}

}
