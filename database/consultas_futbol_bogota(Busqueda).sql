-- ============================================================
-- La CanchaRola / futbol_bogota
-- Consultas de ejemplo. Ejecutar DESPUES de crear la base y cargar los datos
-- (y, para las consultas 2, 3, 4, 5, 10 y 11, despues de finalizar la fase de grupos).
-- ============================================================
USE futbol_bogota;
SET NAMES utf8mb4;

-- 1. Equipos de cada grupo del torneo (JOIN de cuatro tablas)
SELECT g.fase, g.letra AS grupo, e.nombre AS equipo
FROM equ_gru eg
JOIN grupo g ON g.id_grupo = eg.grupo_id_grupo AND g.torneo_id_torneo = eg.grupo_id_torneo
JOIN equipo e ON e.id_equipo = eg.equ_tor_equipo_id_equipo
ORDER BY g.fase, g.letra, e.nombre;

-- 2. Tabla de posiciones por grupo (CTE, UNION ALL, agregaciones)
WITH resultados AS (
    SELECT pe.equ_tor_id_equipo AS id_equipo,
           CASE pe.rol WHEN 'LOCAL' THEN p.goles_local ELSE p.goles_visitante END AS gf,
           CASE pe.rol WHEN 'LOCAL' THEN p.goles_visitante ELSE p.goles_local END AS gc
    FROM partido p
    JOIN par_equ pe ON pe.partido_id_partido = p.id_partido
    WHERE p.estado = 'FINALIZADO' AND p.fase = 'Grupos'
)
SELECT g.letra AS grupo, e.nombre AS equipo,
       COUNT(*) AS pj,
       SUM(r.gf > r.gc) AS pg, SUM(r.gf = r.gc) AS pe, SUM(r.gf < r.gc) AS pp,
       SUM(r.gf) AS gf, SUM(r.gc) AS gc, SUM(r.gf) - SUM(r.gc) AS dg,
       SUM(r.gf > r.gc) * 3 + SUM(r.gf = r.gc) AS puntos
FROM resultados r
JOIN equ_gru eg ON eg.equ_tor_equipo_id_equipo = r.id_equipo
JOIN grupo g ON g.id_grupo = eg.grupo_id_grupo AND g.torneo_id_torneo = eg.grupo_id_torneo
JOIN equipo e ON e.id_equipo = r.id_equipo
GROUP BY g.letra, e.id_equipo, e.nombre
ORDER BY g.letra, puntos DESC, dg DESC, gf DESC;

-- 3. Los 10 maximos goleadores del torneo
SELECT j.primer_nombre, j.primer_apellido, e.nombre AS equipo, COUNT(*) AS goles
FROM evento_partido ev
JOIN inscripcion_jugador ij ON ij.jugador_id_jugador = ev.alineacion_id_jugador
                           AND ij.equ_tor_equipo_id_equipo = ev.alineacion_id_equipo
                           AND ij.equ_tor_id_torneo = ev.alineacion_id_torneo
JOIN jugador j ON j.id_jugador = ij.jugador_id_jugador
JOIN equipo e ON e.id_equipo = ij.equ_tor_equipo_id_equipo
WHERE ev.tipo_evento = 'GOL'
GROUP BY j.id_jugador, j.primer_nombre, j.primer_apellido, e.nombre
ORDER BY goles DESC, j.primer_apellido
LIMIT 10;

-- 4. Tarjetas y faltas por equipo (suma condicional)
SELECT e.nombre AS equipo,
       SUM(ev.tipo_evento = 'AMARILLA') AS amarillas,
       SUM(ev.tipo_evento = 'ROJA') AS rojas,
       SUM(ev.tipo_evento = 'FALTA') AS faltas
FROM evento_partido ev
JOIN equipo e ON e.id_equipo = ev.alineacion_id_equipo
GROUP BY e.id_equipo, e.nombre
HAVING amarillas + rojas + faltas > 0
ORDER BY rojas DESC, amarillas DESC, faltas DESC;

-- 5. Las 5 mayores goleadas (subconsulta escalar)
SELECT p.id_partido, p.fase, el.nombre AS local, p.goles_local, p.goles_visitante, ev.nombre AS visitante,
       ABS(p.goles_local - p.goles_visitante) AS diferencia
FROM partido p
JOIN par_equ pl ON pl.partido_id_partido = p.id_partido AND pl.rol = 'LOCAL'
JOIN par_equ pv ON pv.partido_id_partido = p.id_partido AND pv.rol = 'VISITANTE'
JOIN equipo el ON el.id_equipo = pl.equ_tor_id_equipo
JOIN equipo ev ON ev.id_equipo = pv.equ_tor_id_equipo
WHERE p.estado = 'FINALIZADO'
  AND ABS(p.goles_local - p.goles_visitante) >=
      (SELECT MAX(ABS(goles_local - goles_visitante)) - 1 FROM partido WHERE estado = 'FINALIZADO')
ORDER BY diferencia DESC, p.id_partido
LIMIT 5;

-- 6. Calendario: partidos con equipos, cancha y localidad
SELECT p.id_partido, p.fecha_hora, p.fase, p.jornada, el.nombre AS local, evi.nombre AS visitante,
       c.nombre AS cancha, c.localidad, p.estado
FROM partido p
JOIN par_equ pl ON pl.partido_id_partido = p.id_partido AND pl.rol = 'LOCAL'
JOIN par_equ pv ON pv.partido_id_partido = p.id_partido AND pv.rol = 'VISITANTE'
JOIN equipo el ON el.id_equipo = pl.equ_tor_id_equipo
JOIN equipo evi ON evi.id_equipo = pv.equ_tor_id_equipo
JOIN cancha c ON c.id_cancha = p.cancha_id_cancha
ORDER BY p.fecha_hora, p.id_partido
LIMIT 20;

-- 7. Arbitros que mas partidos han dirigido como CENTRAL
SELECT a.primer_nombre, a.primer_apellido, a.numero_licencia, COUNT(*) AS partidos_central
FROM par_arb pa
JOIN arbitro a ON a.id_arbitro = pa.arbitro_id_arbitro
WHERE pa.rol = 'CENTRAL'
GROUP BY a.id_arbitro, a.primer_nombre, a.primer_apellido, a.numero_licencia
ORDER BY partidos_central DESC, a.primer_apellido
LIMIT 10;

-- 8. Promedio de estatura y peso de los jugadores por posicion
SELECT posicion, COUNT(*) AS jugadores,
       ROUND(AVG(estatura), 1) AS estatura_promedio_cm, ROUND(AVG(peso), 1) AS peso_promedio_kg
FROM jugador
GROUP BY posicion
ORDER BY jugadores DESC;

-- 9. Edad promedio de los jugadores de cada equipo
SELECT e.nombre AS equipo, COUNT(*) AS jugadores,
       ROUND(AVG(TIMESTAMPDIFF(YEAR, j.fecha_nacimiento, CURDATE())), 1) AS edad_promedio
FROM inscripcion_jugador ij
JOIN jugador j ON j.id_jugador = ij.jugador_id_jugador
JOIN equipo e ON e.id_equipo = ij.equ_tor_equipo_id_equipo
GROUP BY e.id_equipo, e.nombre
ORDER BY edad_promedio DESC
LIMIT 10;

-- 10. Jugadores con mas minutos jugados en el torneo
SELECT j.primer_nombre, j.primer_apellido, e.nombre AS equipo,
       COUNT(*) AS partidos, SUM(a.minutos_jugados) AS minutos
FROM alineacion a
JOIN jugador j ON j.id_jugador = a.inscripcion_jugador_id_jugador
JOIN equipo e ON e.id_equipo = a.par_equ_equ_tor_id_equipo
GROUP BY j.id_jugador, j.primer_nombre, j.primer_apellido, e.nombre
ORDER BY minutos DESC, j.primer_apellido
LIMIT 10;

-- 11. Jugadores que nunca fueron titulares (NOT EXISTS)
SELECT j.primer_nombre, j.primer_apellido, e.nombre AS equipo
FROM alineacion a
JOIN jugador j ON j.id_jugador = a.inscripcion_jugador_id_jugador
JOIN equipo e ON e.id_equipo = a.par_equ_equ_tor_id_equipo
WHERE NOT EXISTS (SELECT 1 FROM alineacion t
                  WHERE t.inscripcion_jugador_id_jugador = a.inscripcion_jugador_id_jugador AND t.titular = 'S')
GROUP BY j.id_jugador, j.primer_nombre, j.primer_apellido, e.nombre
ORDER BY e.nombre, j.primer_apellido
LIMIT 20;

-- 12. Canchas por localidad: cantidad, capacidad promedio y cancha mas usada (subconsulta correlacionada)
SELECT c.localidad, COUNT(*) AS canchas, ROUND(AVG(c.capacidad)) AS capacidad_promedio,
       (SELECT c2.nombre FROM cancha c2
        LEFT JOIN partido p2 ON p2.cancha_id_cancha = c2.id_cancha
        WHERE c2.localidad = c.localidad
        GROUP BY c2.id_cancha, c2.nombre
        ORDER BY COUNT(p2.id_partido) DESC, c2.nombre
        LIMIT 1) AS cancha_mas_usada
FROM cancha c
GROUP BY c.localidad
ORDER BY canchas DESC, c.localidad;

-- 13. Equipos con mas de 16 jugadores inscritos y su entrenador (HAVING + LEFT JOIN)
SELECT e.nombre AS equipo, CONCAT(en.primer_nombre, ' ', en.primer_apellido) AS entrenador, COUNT(*) AS jugadores
FROM inscripcion_jugador ij
JOIN equipo e ON e.id_equipo = ij.equ_tor_equipo_id_equipo
LEFT JOIN entrenador en ON en.equipo_id_equipo = e.id_equipo
GROUP BY e.id_equipo, e.nombre, en.id_entrenador, en.primer_nombre, en.primer_apellido
HAVING COUNT(*) > 16
ORDER BY jugadores DESC, e.nombre;

-- 14. Personas por nacionalidad entre jugadores, entrenadores y arbitros (UNION ALL)
SELECT nacionalidad, SUM(CASE rol WHEN 'Jugador' THEN 1 ELSE 0 END) AS jugadores,
       SUM(CASE rol WHEN 'Entrenador' THEN 1 ELSE 0 END) AS entrenadores,
       SUM(CASE rol WHEN 'Arbitro' THEN 1 ELSE 0 END) AS arbitros, COUNT(*) AS total
FROM (SELECT nacionalidad, 'Jugador' AS rol FROM jugador
      UNION ALL SELECT nacionalidad, 'Entrenador' FROM entrenador
      UNION ALL SELECT nacionalidad, 'Arbitro' FROM arbitro) personas
GROUP BY nacionalidad
ORDER BY total DESC;

-- 15. Jugadores con mas de un correo o telefono (atributos multivaluados)
SELECT * FROM (
    SELECT j.id_jugador, j.primer_nombre, j.primer_apellido,
           (SELECT COUNT(*) FROM correo_jugador cj WHERE cj.jugador_id_jugador = j.id_jugador) AS correos,
           (SELECT COUNT(*) FROM telefono_jugador tj WHERE tj.jugador_id_jugador = j.id_jugador) AS telefonos
    FROM jugador j
) conteo
WHERE correos > 1 OR telefonos > 1
ORDER BY id_jugador
LIMIT 20;

-- 16. Usuarios por rol y estado, y cantidad de torneos que organiza cada uno
SELECT u.rol, u.estado, COUNT(DISTINCT u.id_usuario) AS usuarios, COUNT(t.id_torneo) AS torneos_organizados
FROM usuario u
LEFT JOIN torneo t ON t.usuario_id_usuario = u.id_usuario
GROUP BY u.rol, u.estado
ORDER BY u.rol, u.estado;

-- 17. Actividad registrada en auditoria por usuario y accion
SELECT u.correo, a.accion, COUNT(*) AS veces, MAX(a.fecha_hora) AS ultima_vez
FROM auditoria a
JOIN usuario u ON u.id_usuario = a.usuario_id_usuario
GROUP BY u.correo, a.accion
ORDER BY veces DESC;

-- 18. Cruces de eliminatoria con nombres de equipos
SELECT t.nombre AS torneo, c.ronda, c.orden, el.nombre AS local, ev.nombre AS visitante
FROM cruce c
JOIN torneo t ON t.id_torneo = c.torneo_id_torneo
JOIN equipo el ON el.id_equipo = c.local_id_equipo
JOIN equipo ev ON ev.id_equipo = c.visitante_id_equipo
ORDER BY c.ronda, c.orden;

-- 19. Resultado de las tandas de penales por partido
SELECT p.id_partido, p.fase, e.nombre AS equipo, SUM(pe.convertido = 'S') AS convertidos, COUNT(*) AS lanzados
FROM penal pe
JOIN partido p ON p.id_partido = pe.partido_id_partido
JOIN equipo e ON e.id_equipo = pe.equipo_id_equipo
GROUP BY p.id_partido, p.fase, e.id_equipo, e.nombre
ORDER BY p.id_partido, e.nombre;

-- 20. Equipos inscritos en el torneo con estado de pago
SELECT e.nombre AS equipo, et.estado_inscripcion, et.fecha_inscripcion
FROM equ_tor et
JOIN equipo e ON e.id_equipo = et.equipo_id_equipo
ORDER BY et.estado_inscripcion, e.nombre
LIMIT 20;
