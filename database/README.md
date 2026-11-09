# La CanchaRola – Scripts de la base de datos `futbol_bogota`

Scripts de implementación del modelo lógico al modelo físico en MySQL para el sistema de gestión de torneos de fútbol de Bogotá.

## Requisitos

- MySQL 8.0.16 o superior (necesario para que las restricciones `CHECK` se apliquen).
- Un cliente SQL (MySQL Workbench, DBeaver) o la consola `mysql`.
- Los scripts usan el juego de caracteres `utf8mb4`.

## Contenido

| Carpeta / archivo | Tipo | Descripción |
|---|---|---|
| `futbol_bogota_mysql.sql` | Creación de objetos | Crea la base `futbol_bogota`, las 26 tablas, llaves primarias, únicas, foráneas y restricciones `CHECK`. |
| `datos_prueba_futbol_bogota.sql` | Inserción de registros | Carga los datos de prueba. |
| `finalizar_fase_grupos_futbol_bogota.sql` | Scripts varios | Simula los 72 partidos de la fase de grupos con alineaciones, goles y tarjetas. |
| `consultas_futbol_bogota.sql` | Scripts varios | 20 consultas de ejemplo sobre los datos. |

## Orden de ejecución

1. `futbol_bogota_mysql.sql`
2. `datos_prueba_futbol_bogota.sql`
3. `finalizar_fase_grupos_futbol_bogota.sql` (opcional)
4. `consultas_futbol_bogota.sql`

Desde consola:

```bash
mysql -u root -p < futbol_bogota_mysql.sql
mysql -u root -p < datos_prueba_futbol_bogota.sql
mysql -u root -p < finalizar_fase_grupos_futbol_bogota.sql
mysql -u root -p < consultas_futbol_bogota.sql
```

En MySQL Workbench: abrir cada archivo con *File > Open SQL Script* y ejecutarlo completo con el rayo.

---

## 1. Creación de objetos – `futbol_bogota_mysql.sql`

- Crea la base `futbol_bogota` (`utf8mb4`, `utf8mb4_unicode_ci`) y las 26 tablas con motor InnoDB.
- Los identificadores propios usan `AUTO_INCREMENT`.
- Cada columna lleva su `COMMENT` con la descripción del diccionario de datos.
- Las llaves foráneas se crean al final con `ALTER TABLE`, para no depender del orden de creación de las tablas.
- Incluye las tablas `cruce` (cruces de las rondas eliminatorias) y `penal` (tiros de la tanda de penales de un partido).
- Para empezar de cero: `DROP DATABASE futbol_bogota;` y volver a ejecutar el script.

## 2. Datos de prueba – `datos_prueba_futbol_bogota.sql`

Carga un torneo completo, listo para usar en la aplicación:

| Elemento | Cantidad |
|---|---|
| Torneos | 1 |
| Equipos (12 grupos de 4) | 48 |
| Jugadores (inscritos, 17 por equipo) | 816 |
| Entrenadores | 48 |
| Árbitros | 40 |
| Canchas | 20 |
| Partidos de fase de grupos (estado PROGRAMADO) | 72 |
| Usuarios | 5 |

Además inserta los correos y teléfonos de jugadores, entrenadores y árbitros, la inscripción de los equipos, los grupos y los árbitros y equipos de cada partido.

**Usuarios de prueba** (clave de todos: `Prueba12345`):

| Correo | Rol |
|---|---|
| `admin@canchasrola.test` | ADMIN |
| `organizador@canchasrola.test` | ORGANIZADOR |
| `espectador1@canchasrola.test`, `espectador2@canchasrola.test`, `espectador3@canchasrola.test` | ESPECTADOR |

**Advertencias**

- Este script **vacía todas las tablas** (`TRUNCATE`) antes de insertar; no lo ejecutes sobre datos que quieras conservar.
- Debe ejecutarse después de crear el esquema.
- El escudo de cada equipo se inserta como una imagen PNG en hexadecimal (`X'89504E47...'`), porque la columna `equipo.escudo` es `LONGBLOB NOT NULL` y la aplicación muestra el escudo. Por eso el archivo es grande (cerca de 500 KB) y tiene líneas muy largas; es normal.

## 3. Scripts varios

### `finalizar_fase_grupos_futbol_bogota.sql`

- Ejecutar **después** de `datos_prueba_futbol_bogota.sql`.
- Crea las alineaciones (17 jugadores por equipo), los eventos (goles, tarjetas y faltas) y los marcadores, y pasa los 72 partidos a `FINALIZADO`.
- Se puede ejecutar varias veces: borra las alineaciones y eventos anteriores y los vuelve a crear.
- Al terminar muestra la tabla de posiciones, los mejores terceros y los goleadores.

### `consultas_futbol_bogota.sql`

Consultas de ejemplo, de solo lectura. Las consultas 2, 3, 4, 5, 10 y 11 necesitan que la fase de grupos esté finalizada. Las consultas 15 a 19 pueden devolver pocas o ninguna fila con los datos de prueba (por ejemplo, `cruce`, `penal` y `auditoria` empiezan vacías).

| N.º | Qué muestra | Técnica |
|---|---|---|
| 1 | Equipos de cada grupo | JOIN de cuatro tablas |
| 2 | Tabla de posiciones por grupo | CTE, UNION ALL, funciones de agregación |
| 3 | Los 10 máximos goleadores | JOIN con llave compuesta, GROUP BY, LIMIT |
| 4 | Tarjetas y faltas por equipo | Suma condicional, HAVING |
| 5 | Las 5 mayores goleadas | Subconsulta escalar |
| 6 | Calendario con equipos, cancha y localidad | JOIN de varias tablas |
| 7 | Árbitros con más partidos como central | JOIN, GROUP BY |
| 8 | Estatura y peso promedio por posición | AVG, GROUP BY |
| 9 | Edad promedio de los jugadores por equipo | TIMESTAMPDIFF, AVG |
| 10 | Jugadores con más minutos jugados | SUM, GROUP BY |
| 11 | Jugadores que nunca fueron titulares | NOT EXISTS |
| 12 | Canchas por localidad y cancha más usada | Subconsulta correlacionada |
| 13 | Equipos con más de 16 jugadores y su entrenador | LEFT JOIN, HAVING |
| 14 | Personas por nacionalidad (jugadores, entrenadores, árbitros) | UNION ALL, tabla derivada |
| 15 | Jugadores con más de un correo o teléfono | Subconsultas, atributos multivaluados |
| 16 | Usuarios por rol y estado, y torneos que organizan | LEFT JOIN, COUNT DISTINCT |
| 17 | Actividad registrada en auditoría | JOIN, GROUP BY, MAX |
| 18 | Cruces de eliminatoria con nombres de equipos | JOIN de cuatro tablas |
| 19 | Resultado de las tandas de penales | SUM condicional |
| 20 | Equipos inscritos y estado de pago | JOIN, ORDER BY |
