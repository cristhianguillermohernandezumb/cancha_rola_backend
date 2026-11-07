-- ============================================================
-- Base de datos: torneos de fútbol (MySQL 8.0.16 o superior)
-- Convertido desde prueba_final_6.ddl (Oracle SQL Developer Data Modeler).
-- Los ids propios usan AUTO_INCREMENT en lugar de secuencias.
-- ============================================================

CREATE DATABASE IF NOT EXISTS futbol_bogota
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE futbol_bogota;

CREATE TABLE alineacion (
    titular CHAR(1) NOT NULL COMMENT 'Si un jugador es titular o no',
    minutos_jugados INT NOT NULL COMMENT 'minutos que jugó un jugador',
    par_equ_partido_id_partido BIGINT NOT NULL,
    par_equ_equ_tor_id_equipo BIGINT NOT NULL,
    par_equ_equ_tor_id_torneo BIGINT NOT NULL,
    inscripcion_jugador_id_jugador BIGINT NOT NULL,
    CONSTRAINT alineacion_ck_1 CHECK (titular IN ('S','N')),
    CONSTRAINT alineacion_ck_2 CHECK (minutos_jugados >= 0),
    CONSTRAINT alineacion_pk PRIMARY KEY (par_equ_partido_id_partido, par_equ_equ_tor_id_equipo, par_equ_equ_tor_id_torneo, inscripcion_jugador_id_jugador),
    CONSTRAINT alineacion__un UNIQUE (par_equ_partido_id_partido, inscripcion_jugador_id_jugador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE arbitro (
    id_arbitro BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de un participante (arbitro)',
    documento VARCHAR(50) NOT NULL COMMENT 'documento de identidad de un arbitro',
    tipo_documento VARCHAR(15) NOT NULL COMMENT 'tipo de documento de un arbitro (cedula, cedula extrangera)',
    primer_nombre VARCHAR(50) NOT NULL COMMENT 'primer nombre de un arbitro',
    segundo_nombre VARCHAR(50) NOT NULL COMMENT 'segundo nombre de un arbitro',
    primer_apellido VARCHAR(50) NOT NULL COMMENT 'primer apellido de un arbitro',
    segundo_apellido VARCHAR(50) NOT NULL COMMENT 'segundo apellido de un arbitro',
    fecha_nacimiento DATE NOT NULL COMMENT 'fecha de nacimiento del arbitro',
    numero_licencia INT NOT NULL COMMENT 'numeor de la licencia de un arbitro',
    nacionalidad VARCHAR(50) NOT NULL COMMENT 'nacionalidad de un arbitro',
    estado VARCHAR(50) NOT NULL COMMENT 'estado de un arbitro (Activo, Inactivo)',
    CONSTRAINT arbitro_ck_1 CHECK (estado IN ('ACTIVO','INACTIVO')),
    CONSTRAINT arbitro_ck_2 CHECK (tipo_documento IN ('CC','CE','TI','PAS')),
    CONSTRAINT arbitro_pk PRIMARY KEY (id_arbitro),
    CONSTRAINT arbitro__un UNIQUE (tipo_documento, documento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE auditoria (
    id_auditoria BIGINT NOT NULL AUTO_INCREMENT COMMENT 'identificador unico de auditoria',
    detalle VARCHAR(100) COMMENT 'detalle de una auditoria',
    fecha_hora DATETIME NOT NULL COMMENT 'fecha en la que un operario realiza una modificacion',
    entidad VARCHAR(30) NOT NULL COMMENT 'nombre de la entidad cambiada',
    id_registro VARCHAR(100) NOT NULL COMMENT 'identificador de un registro',
    accion VARCHAR(50) NOT NULL COMMENT 'accion realizada para vereficar una modificacion en el sistema',
    usuario_id_usuario BIGINT NOT NULL,
    torneo_id_torneo BIGINT,
    CONSTRAINT auditoria_ck_1 CHECK (accion IN ('CREAR','MODIFICAR','ELIMINAR','CAMBIAR_ESTADO','LOGIN')),
    CONSTRAINT auditoria_pk PRIMARY KEY (id_auditoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE cancha (
    id_cancha BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de cancha',
    nombre VARCHAR(50) NOT NULL COMMENT 'nombre de una cancha',
    direccion VARCHAR(50) NOT NULL COMMENT 'ubicacion de una cancha',
    tipo_superficie VARCHAR(50) NOT NULL COMMENT 'tipo de superficie de la cancha',
    localidad VARCHAR(50) NOT NULL COMMENT 'Localidad en donde se encuentra la cancha',
    capacidad INT NOT NULL COMMENT 'cantidad de personas que puede contener una cancha',
    largo INT NOT NULL COMMENT 'longitud en metros de una cancha',
    ancho INT NOT NULL COMMENT 'ancho en metros de una cancha',
    CONSTRAINT cancha_ck_1 CHECK (tipo_superficie IN ('SINTETICA','NATURAL','CEMENTO')),
    CONSTRAINT cancha_ck_2 CHECK (capacidad > 0 AND largo > 0 AND ancho > 0),
    CONSTRAINT cancha_pk PRIMARY KEY (id_cancha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE correo_arbitro (
    correo_arbitro VARCHAR(100) NOT NULL COMMENT 'correo de un arbitro',
    arbitro_id_arbitro BIGINT NOT NULL,
    CONSTRAINT correo_arbitro_pk PRIMARY KEY (correo_arbitro, arbitro_id_arbitro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE correo_entrenador (
    correo_entrenador VARCHAR(100) NOT NULL COMMENT 'correo de un entrenador',
    entrenador_id_entrenador BIGINT NOT NULL,
    CONSTRAINT correo_entrenador_pk PRIMARY KEY (correo_entrenador, entrenador_id_entrenador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE correo_jugador (
    correo_jugador VARCHAR(100) NOT NULL COMMENT 'correo de un jugador',
    jugador_id_jugador BIGINT NOT NULL,
    CONSTRAINT correo_jugador_pk PRIMARY KEY (correo_jugador, jugador_id_jugador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE entrenador (
    id_entrenador BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de un entrenador',
    documento VARCHAR(50) NOT NULL COMMENT 'documento de identidad de un entrenador',
    primer_nombre VARCHAR(50) NOT NULL COMMENT 'primer nombre de un entrenador',
    segundo_nombre VARCHAR(50) NOT NULL COMMENT 'segundo nombre de un entrenador',
    primer_apellido VARCHAR(50) NOT NULL COMMENT 'primer apellido de un cuerpo entrenador',
    segundo_apellido VARCHAR(50) NOT NULL COMMENT 'segundo apellido de un cuerpo entrenador',
    fecha_nacimiento DATE NOT NULL COMMENT 'fecha de nacimiento de un entrenador',
    anios_experiencia INT NOT NULL,
    tipo_documento VARCHAR(50) NOT NULL COMMENT 'tipo de documento de un entrenador (cedula, cedula extranjeria)',
    nacionalidad VARCHAR(50) NOT NULL COMMENT 'nacionalidad de un entrenador',
    equipo_id_equipo BIGINT NOT NULL,
    CONSTRAINT entrenador_ck_1 CHECK (tipo_documento IN ('CC','CE','TI','PAS')),
    CONSTRAINT entrenador_ck_2 CHECK (anios_experiencia >= 0),
    CONSTRAINT entrenador_pk PRIMARY KEY (id_entrenador),
    CONSTRAINT entrenador__un UNIQUE (documento, tipo_documento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE equ_gru (
    grupo_id_grupo BIGINT NOT NULL,
    grupo_id_torneo BIGINT NOT NULL,
    equ_tor_equipo_id_equipo BIGINT NOT NULL,
    CONSTRAINT equ_gru_pk PRIMARY KEY (grupo_id_grupo, grupo_id_torneo, equ_tor_equipo_id_equipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE equ_tor (
    equipo_id_equipo BIGINT NOT NULL,
    torneo_id_torneo BIGINT NOT NULL,
    estado_inscripcion VARCHAR(20) NOT NULL COMMENT 'en que estado esta una inscripcion de un equipo (pagada, sin pagar)',
    fecha_inscripcion DATE NOT NULL COMMENT 'fecha en la que un equipo se inscribio',
    CONSTRAINT equ_tor_ck_1 CHECK (estado_inscripcion IN ('PAGADA','SIN_PAGAR')),
    CONSTRAINT equ_tor_pk PRIMARY KEY (equipo_id_equipo, torneo_id_torneo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE equipo (
    id_equipo BIGINT NOT NULL AUTO_INCREMENT COMMENT 'identificador  unico de equipo',
    nombre VARCHAR(50) NOT NULL COMMENT 'nombre de un equipo',
    fecha_fundacion DATE NOT NULL COMMENT 'fecha de fundacion del equipo',
    correo VARCHAR(100) NOT NULL COMMENT 'correo de contacto de un equipo',
    telefono_equipo BIGINT NOT NULL COMMENT 'telefono de contacto de un equipo',
    escudo LONGBLOB NOT NULL COMMENT 'Imagen respectiva a el escudo de un equipo',
    CONSTRAINT equipo_pk PRIMARY KEY (id_equipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE evento_partido (
    id_evento BIGINT NOT NULL AUTO_INCREMENT COMMENT 'identificador unico de evento',
    minuto INT NOT NULL COMMENT 'minuto en el que sucedio un evento',
    tipo_evento VARCHAR(50) NOT NULL COMMENT 'evento de un partido (tarjeta amarilla, tarjeta roja, falta, gol, etc)',
    partido_id_partido BIGINT NOT NULL,
    alineacion_id_equipo BIGINT NOT NULL,
    alineacion_id_torneo BIGINT NOT NULL,
    alineacion_id_jugador BIGINT NOT NULL,
    CONSTRAINT evento_partido_ck_1 CHECK (tipo_evento IN ('GOL','AUTOGOL','AMARILLA','ROJA','FALTA')),
    CONSTRAINT evento_partido_ck_2 CHECK (minuto >= 0),
    CONSTRAINT evento_partido_pk PRIMARY KEY (partido_id_partido, id_evento),
    KEY ix_evento_partido_id_evento (id_evento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE grupo (
    id_grupo BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de un grupo',
    fase INT NOT NULL COMMENT 'En que momento de un torneo se esta',
    letra CHAR(1) NOT NULL COMMENT 'letra que identifica a un grupo en un torneo',
    torneo_id_torneo BIGINT NOT NULL,
    CONSTRAINT grupo_ck_1 CHECK (fase > 0),
    CONSTRAINT grupo_ck_2 CHECK (letra BETWEEN 'A' AND 'Z'),
    CONSTRAINT grupo_pk PRIMARY KEY (id_grupo, torneo_id_torneo),
    CONSTRAINT grupo__un UNIQUE (torneo_id_torneo, fase, letra)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE inscripcion_jugador (
    numero_camiseta INT NOT NULL COMMENT 'numero de camiseta de un jugador en una inscipcion a un equipo en un torneo',
    jugador_id_jugador BIGINT NOT NULL,
    equ_tor_equipo_id_equipo BIGINT NOT NULL,
    equ_tor_id_torneo BIGINT NOT NULL,
    CONSTRAINT inscripcion_jugador_ck_1 CHECK (numero_camiseta BETWEEN 0 AND 99),
    CONSTRAINT inscripcion_jugador_pk PRIMARY KEY (jugador_id_jugador, equ_tor_equipo_id_equipo, equ_tor_id_torneo),
    CONSTRAINT ins_camiseta_uk UNIQUE (equ_tor_equipo_id_equipo, equ_tor_id_torneo, numero_camiseta),
    CONSTRAINT inscripcion_jugador__un UNIQUE (jugador_id_jugador, equ_tor_id_torneo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE jugador (
    id_jugador BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de un jugador',
    documento VARCHAR(50) NOT NULL COMMENT 'documento de identidad de un jugador',
    primer_nombre VARCHAR(50) NOT NULL COMMENT 'primer nombre de un jugador',
    segundo_nombre VARCHAR(50) NOT NULL COMMENT 'segundo nombre de un jugador',
    primer_apellido VARCHAR(50) NOT NULL COMMENT 'primer apellido de un jugador',
    segundo_apellido VARCHAR(50) NOT NULL COMMENT 'segundo apellido de un jugador',
    fecha_nacimiento DATE NOT NULL COMMENT 'fecha de nacimiento del jugador',
    estatura INT NOT NULL COMMENT 'estatura de un jugadoren cm',
    peso DECIMAL(5,2) NOT NULL COMMENT 'peso de un jugador en kg',
    pierna_habil VARCHAR(15) NOT NULL COMMENT 'dato de la pierna habil de un jugador',
    tipo_documento VARCHAR(50) NOT NULL COMMENT 'tipo de documento de un jugador (cedula, cedula extrangera, etc)',
    nacionalidad VARCHAR(50) NOT NULL,
    estado VARCHAR(15) NOT NULL COMMENT 'estado de un jugador (''''ACTIVO'''',''''LESIONADO'''',''''SUSPENDIDO'''')',
    posicion VARCHAR(50) NOT NULL COMMENT 'posicion en la que juega un usuario',
    CONSTRAINT jugador_ck_1 CHECK (tipo_documento IN ('CC','CE','TI','PAS')),
    CONSTRAINT jugador_ck_2 CHECK (estado IN ('ACTIVO','LESIONADO','SUSPENDIDO')),
    CONSTRAINT jugador_ck_3 CHECK (posicion IN ('PORTERO','DEFENSA','VOLANTE','DELANTERO')),
    CONSTRAINT jugador_ck_4 CHECK (pierna_habil IN ('DERECHA','IZQUIERDA','AMBIDIESTRO')),
    CONSTRAINT jugador_ck_5 CHECK (estatura > 0 AND peso > 0),
    CONSTRAINT jugador_pk PRIMARY KEY (id_jugador),
    CONSTRAINT jugador__un UNIQUE (documento, tipo_documento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE par_arb (
    partido_id_partido BIGINT NOT NULL,
    arbitro_id_arbitro BIGINT NOT NULL,
    rol VARCHAR(20) NOT NULL COMMENT 'rol de un arbitro en un partido (central, asistente 1, asistente 2, cuarto árbitro)',
    CONSTRAINT par_arb_ck_1 CHECK (rol IN ('CENTRAL','ASISTENTE_1','ASISTENTE_2','CUARTO','VAR')),
    CONSTRAINT par_arb_pk PRIMARY KEY (partido_id_partido, arbitro_id_arbitro),
    CONSTRAINT par_arb__un UNIQUE (partido_id_partido, rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE par_equ (
    partido_id_partido BIGINT NOT NULL,
    rol VARCHAR(20) NOT NULL COMMENT 'rol que tiene un equipo en un partido: local/visitante',
    equ_tor_id_equipo BIGINT NOT NULL,
    equ_tor_id_torneo BIGINT NOT NULL,
    CONSTRAINT par_equ_ck_1 CHECK (rol IN ('LOCAL','VISITANTE')),
    CONSTRAINT par_equ_pk PRIMARY KEY (partido_id_partido, equ_tor_id_equipo, equ_tor_id_torneo),
    CONSTRAINT par_equ__un UNIQUE (partido_id_partido, rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE partido (
    id_partido BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de un partido',
    fecha_hora DATETIME NOT NULL COMMENT 'fecha y hora donde se realiza un partido',
    estado VARCHAR(50) NOT NULL COMMENT 'estado del partido: ''''PROGRAMADO'''',''''EN_JUEGO'''',''''FINALIZADO'''',''''APLAZADO''''',
    goles_local INT DEFAULT 0 NOT NULL COMMENT 'goles que anotó el equipo de local',
    goles_visitante INT DEFAULT 0 NOT NULL COMMENT 'goles que anotó el equipo de visitante',
    fase VARCHAR(50) NOT NULL COMMENT 'fase en la que se encuentra un partido (grupos,cuartos,semis,final, etc)',
    jornada INT NOT NULL COMMENT 'jornada en la que se juega un partido ',
    cancha_id_cancha BIGINT NOT NULL,
    torneo_id_torneo BIGINT NOT NULL,
    CONSTRAINT partido_ck_1 CHECK (estado IN ('PROGRAMADO','EN_JUEGO','FINALIZADO','APLAZADO')),
    CONSTRAINT partido_ck_2 CHECK (goles_local >= 0 AND goles_visitante >= 0),
    CONSTRAINT partido_ck_3 CHECK (jornada > 0),
    CONSTRAINT partido_pk PRIMARY KEY (id_partido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE telefono_arbitro (
    telefono_arbitro VARCHAR(20) NOT NULL COMMENT 'numero telefonico de un arbitro',
    arbitro_id_arbitro BIGINT NOT NULL,
    CONSTRAINT telefono_arbitro_pk PRIMARY KEY (arbitro_id_arbitro, telefono_arbitro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE telefono_entrenador (
    telefono_entrenador VARCHAR(20) NOT NULL COMMENT 'numero telefonico de un entrenador',
    entrenador_id_entrenador BIGINT NOT NULL,
    CONSTRAINT telefono_entrenador_pk PRIMARY KEY (entrenador_id_entrenador, telefono_entrenador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE telefono_jugador (
    telefono_jugador VARCHAR(20) NOT NULL COMMENT 'numero telefonico de un jugador',
    jugador_id_jugador BIGINT NOT NULL,
    CONSTRAINT telefono_jugador_pk PRIMARY KEY (telefono_jugador, jugador_id_jugador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tor_usu (
    usuario_id_usuario BIGINT NOT NULL,
    torneo_id_torneo BIGINT NOT NULL,
    fecha_seguimiento DATE NOT NULL COMMENT 'fecha en la que un usuario sigui un partido',
    notificacion CHAR(1) NOT NULL COMMENT 'si un usuario quiere tener notificaciones de cuando un torneo inicie',
    CONSTRAINT tor_usu_ck_1 CHECK (notificacion IN ('S','N')),
    CONSTRAINT tor_usu_pk PRIMARY KEY (torneo_id_torneo, usuario_id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE torneo (
    id_torneo BIGINT NOT NULL AUTO_INCREMENT COMMENT 'identificador unico de un torneo',
    nombre VARCHAR(50) NOT NULL COMMENT 'nombre de un torneo',
    fecha_inicio DATE NOT NULL COMMENT 'fecha de inicio de un torneo',
    fecha_fin DATE NOT NULL COMMENT 'fecha de fin de un torneo',
    estado VARCHAR(15) NOT NULL COMMENT 'estado de un partido: Iniciado/terminado',
    telefono BIGINT NOT NULL COMMENT 'telefono de contacto de un torneo',
    modalidad VARCHAR(20) NOT NULL COMMENT 'tipo de modadlidad en un torneo (futbol 8, futbol 11, etc...)',
    categoria VARCHAR(20) NOT NULL COMMENT 'tipo de categoria de un torneo (sub 20, sub 18, etc...)',
    costo_inscripcion DECIMAL(12,2) NOT NULL COMMENT 'Costo de inscripcion de un torneo  ',
    max_equipos INT NOT NULL COMMENT 'cantidad de equipos que pueden paticipar en un torneo',
    usuario_id_usuario BIGINT NOT NULL,
    CONSTRAINT torneo_ck_1 CHECK (fecha_fin >= fecha_inicio),
    CONSTRAINT torneo_ck_2 CHECK (estado IN ('INSCRIPCION','EN_CURSO','FINALIZADO')),
    CONSTRAINT torneo_ck_3 CHECK (modalidad IN ('FUTBOL_5','FUTBOL_7','FUTBOL_8','FUTBOL_11')),
    CONSTRAINT torneo_ck_4 CHECK (max_equipos > 0 AND costo_inscripcion >= 0),
    CONSTRAINT torneo_pk PRIMARY KEY (id_torneo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE usuario (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de usuario',
    primer_nombre VARCHAR(50) NOT NULL COMMENT 'primer nombre de el usuario',
    segundo_nombre VARCHAR(50) NOT NULL COMMENT 'segundo nombre de el usuario',
    primer_apellido VARCHAR(50) NOT NULL COMMENT 'Primer apellido de el usuario',
    segundo_apellido VARCHAR(50) NOT NULL COMMENT 'segundo apellido de el usuario',
    fecha_registro DATE NOT NULL COMMENT 'fecha en la que se registro un usuario en la aplicacion web',
    contrasenia VARCHAR(300) NOT NULL COMMENT 'contraseña de un usuario con respecto  a su registro',
    rol VARCHAR(20) NOT NULL COMMENT 'rol de un usuario en la aplicacion (cliente, organizador y administrador',
    telefono BIGINT COMMENT 'telefono de un usuario ',
    correo VARCHAR(100) NOT NULL COMMENT 'correo d eun usuario',
    estado VARCHAR(15) NOT NULL COMMENT 'el estado de un usuario (activo o inactivo)',
    CONSTRAINT usuario_ck_1 CHECK (rol IN ('ADMIN','ORGANIZADOR','ESPECTADOR')),
    CONSTRAINT usuario_ck_2 CHECK (estado IN ('ACTIVO','INACTIVO')),
    CONSTRAINT usuario_pk PRIMARY KEY (id_usuario),
    CONSTRAINT usuario__un UNIQUE (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Llaves foráneas
ALTER TABLE alineacion
    ADD CONSTRAINT ali_inscripcion_fk FOREIGN KEY (inscripcion_jugador_id_jugador, par_equ_equ_tor_id_equipo, par_equ_equ_tor_id_torneo) REFERENCES inscripcion_jugador (jugador_id_jugador, equ_tor_equipo_id_equipo, equ_tor_id_torneo);

ALTER TABLE alineacion
    ADD CONSTRAINT alineacion_par_equ_fk FOREIGN KEY (par_equ_partido_id_partido, par_equ_equ_tor_id_equipo, par_equ_equ_tor_id_torneo) REFERENCES par_equ (partido_id_partido, equ_tor_id_equipo, equ_tor_id_torneo);

ALTER TABLE auditoria
    ADD CONSTRAINT auditoria_torneo_fk FOREIGN KEY (torneo_id_torneo) REFERENCES torneo (id_torneo);

ALTER TABLE auditoria
    ADD CONSTRAINT auditoria_usuario_fk FOREIGN KEY (usuario_id_usuario) REFERENCES usuario (id_usuario);

ALTER TABLE correo_entrenador
    ADD CONSTRAINT cor_entrenador_fk FOREIGN KEY (entrenador_id_entrenador) REFERENCES entrenador (id_entrenador);

ALTER TABLE correo_arbitro
    ADD CONSTRAINT correo_arbitro_arbitro_fk FOREIGN KEY (arbitro_id_arbitro) REFERENCES arbitro (id_arbitro);

ALTER TABLE correo_jugador
    ADD CONSTRAINT correo_jugador_jugador_fk FOREIGN KEY (jugador_id_jugador) REFERENCES jugador (id_jugador);

ALTER TABLE entrenador
    ADD CONSTRAINT entrenador_equipo_fk FOREIGN KEY (equipo_id_equipo) REFERENCES equipo (id_equipo);

ALTER TABLE equ_gru
    ADD CONSTRAINT equ_gru_equ_tor_fk FOREIGN KEY (equ_tor_equipo_id_equipo, grupo_id_torneo) REFERENCES equ_tor (equipo_id_equipo, torneo_id_torneo);

ALTER TABLE equ_gru
    ADD CONSTRAINT equ_gru_grupo_fk FOREIGN KEY (grupo_id_grupo, grupo_id_torneo) REFERENCES grupo (id_grupo, torneo_id_torneo);

ALTER TABLE equ_tor
    ADD CONSTRAINT equ_tor_equipo_fk FOREIGN KEY (equipo_id_equipo) REFERENCES equipo (id_equipo);

ALTER TABLE equ_tor
    ADD CONSTRAINT equ_tor_torneo_fk FOREIGN KEY (torneo_id_torneo) REFERENCES torneo (id_torneo);

ALTER TABLE evento_partido
    ADD CONSTRAINT evento_partido_alineacion_fk FOREIGN KEY (partido_id_partido, alineacion_id_equipo, alineacion_id_torneo, alineacion_id_jugador) REFERENCES alineacion (par_equ_partido_id_partido, par_equ_equ_tor_id_equipo, par_equ_equ_tor_id_torneo, inscripcion_jugador_id_jugador);

ALTER TABLE evento_partido
    ADD CONSTRAINT evento_partido_partido_fk FOREIGN KEY (partido_id_partido) REFERENCES partido (id_partido);

ALTER TABLE grupo
    ADD CONSTRAINT grupo_torneo_fk FOREIGN KEY (torneo_id_torneo) REFERENCES torneo (id_torneo);

ALTER TABLE inscripcion_jugador
    ADD CONSTRAINT inscripcion_jugador_equ_tor_fk FOREIGN KEY (equ_tor_equipo_id_equipo, equ_tor_id_torneo) REFERENCES equ_tor (equipo_id_equipo, torneo_id_torneo);

ALTER TABLE inscripcion_jugador
    ADD CONSTRAINT inscripcion_jugador_jugador_fk FOREIGN KEY (jugador_id_jugador) REFERENCES jugador (id_jugador);

ALTER TABLE par_arb
    ADD CONSTRAINT par_arb_arbitro_fk FOREIGN KEY (arbitro_id_arbitro) REFERENCES arbitro (id_arbitro);

ALTER TABLE par_arb
    ADD CONSTRAINT par_arb_partido_fk FOREIGN KEY (partido_id_partido) REFERENCES partido (id_partido);

ALTER TABLE par_equ
    ADD CONSTRAINT par_equ_equ_tor_fk FOREIGN KEY (equ_tor_id_equipo, equ_tor_id_torneo) REFERENCES equ_tor (equipo_id_equipo, torneo_id_torneo);

ALTER TABLE par_equ
    ADD CONSTRAINT par_equ_partido_fk FOREIGN KEY (partido_id_partido) REFERENCES partido (id_partido);

ALTER TABLE partido
    ADD CONSTRAINT partido_cancha_fk FOREIGN KEY (cancha_id_cancha) REFERENCES cancha (id_cancha);

ALTER TABLE partido
    ADD CONSTRAINT partido_torneo_fk FOREIGN KEY (torneo_id_torneo) REFERENCES torneo (id_torneo);

ALTER TABLE telefono_entrenador
    ADD CONSTRAINT tel_entrenador_fk FOREIGN KEY (entrenador_id_entrenador) REFERENCES entrenador (id_entrenador);

ALTER TABLE telefono_arbitro
    ADD CONSTRAINT telefono_arbitro_arbitro_fk FOREIGN KEY (arbitro_id_arbitro) REFERENCES arbitro (id_arbitro);

ALTER TABLE telefono_jugador
    ADD CONSTRAINT telefono_jugador_jugador_fk FOREIGN KEY (jugador_id_jugador) REFERENCES jugador (id_jugador);

ALTER TABLE tor_usu
    ADD CONSTRAINT tor_usu_torneo_fk FOREIGN KEY (torneo_id_torneo) REFERENCES torneo (id_torneo);

ALTER TABLE tor_usu
    ADD CONSTRAINT tor_usu_usuario_fk FOREIGN KEY (usuario_id_usuario) REFERENCES usuario (id_usuario);

ALTER TABLE torneo
    ADD CONSTRAINT torneo_usuario_fk FOREIGN KEY (usuario_id_usuario) REFERENCES usuario (id_usuario);

