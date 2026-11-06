package co.edu.unbosque.service;

import java.util.List;

/**
 * Reglas de una tanda de penales, sin base de datos. Los tiros se patean alternados, empezando por el local
 * (posicion 0 = local, 1 = visitante, 2 = local...). Cada tiro es true (gol) o false (fallo).
 * <ul>
 * <li>Primeras 5 rondas: la tanda termina antes si un equipo ya no puede ser alcanzado por el otro.</li>
 * <li>Si tras 5 tiros cada uno hay empate: muerte subita, se decide al terminar cada ronda de dos tiros.</li>
 * </ul>
 */
public final class TandaPenales {

	public static final int TIROS_INICIALES = 5;

	public record Estado(int golesLocal, int golesVisitante, int tirosLocal, int tirosVisitante, String siguiente,
			String ganador) {
	}

	private TandaPenales() {
	}

	public static Estado calcular(List<Boolean> tiros) {
		int golesLocal = 0;
		int golesVisitante = 0;
		int tirosLocal = 0;
		int tirosVisitante = 0;
		for (int i = 0; i < tiros.size(); i++) {
			boolean gol = Boolean.TRUE.equals(tiros.get(i));
			if (i % 2 == 0) {
				tirosLocal++;
				if (gol) {
					golesLocal++;
				}
			} else {
				tirosVisitante++;
				if (gol) {
					golesVisitante++;
				}
			}
		}
		String ganador = ganador(golesLocal, golesVisitante, tirosLocal, tirosVisitante);
		String siguiente = ganador != null ? null : (tirosLocal == tirosVisitante ? "LOCAL" : "VISITANTE");
		return new Estado(golesLocal, golesVisitante, tirosLocal, tirosVisitante, siguiente, ganador);
	}

	private static String ganador(int gl, int gv, int tl, int tv) {
		if (tl <= TIROS_INICIALES && tv <= TIROS_INICIALES) {
			if (gl > gv + (TIROS_INICIALES - tv)) {
				return "LOCAL";
			}
			if (gv > gl + (TIROS_INICIALES - tl)) {
				return "VISITANTE";
			}
			return null;
		}
		if (tl == tv && gl != gv) {
			return gl > gv ? "LOCAL" : "VISITANTE";
		}
		return null;
	}

}
