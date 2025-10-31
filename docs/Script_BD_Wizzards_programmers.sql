
CREATE SEQUENCE public.administrador_administrador_id_seq_1;

CREATE TABLE public.Administrador (
                Administrador_id INTEGER NOT NULL DEFAULT nextval('public.administrador_administrador_id_seq_1'),
                Password VARCHAR NOT NULL,
                Email VARCHAR NOT NULL,
                CONSTRAINT administrador_pk PRIMARY KEY (Administrador_id)
);


ALTER SEQUENCE public.administrador_administrador_id_seq_1 OWNED BY public.Administrador.Administrador_id;

CREATE TABLE public.Torneo (
                Torneo_id INTEGER NOT NULL,
                Administrador_id INTEGER NOT NULL,
                Nombre VARCHAR NOT NULL,
                Descripcion VARCHAR NOT NULL,
                Fecha_inicio DATE NOT NULL,
                Fecha_fin DATE NOT NULL,
                Esta_publicado BOOLEAN NOT NULL,
                CONSTRAINT torneo_pk PRIMARY KEY (Torneo_id)
);


CREATE SEQUENCE public.participante_usuario_id_seq;

CREATE TABLE public.Participante (
                Participante_id INTEGER NOT NULL DEFAULT nextval('public.participante_usuario_id_seq'),
                Nombre VARCHAR NOT NULL,
                Apellido VARCHAR NOT NULL,
                Tipo VARCHAR NOT NULL,
                dni VARCHAR NOT NULL,
                Email VARCHAR NOT NULL,
                Password VARCHAR NOT NULL,
                CONSTRAINT participante_pk PRIMARY KEY (Participante_id)
);
COMMENT ON COLUMN public.Participante.dni IS 'A ESTO HAY QUE HACERLE UN UNIQUE EN EL SCRIP CUANDO LO PASAMOS A LA BD';


ALTER SEQUENCE public.participante_usuario_id_seq OWNED BY public.Participante.Participante_id;

CREATE UNIQUE INDEX participante_idx
 ON public.Participante
 ( dni );

CREATE UNIQUE INDEX participante_idx1
 ON public.Participante
 ( Email );

CREATE SEQUENCE public.competencia_competencia_id_seq;

CREATE TABLE public.Competencia (
                Competencia_id INTEGER NOT NULL DEFAULT nextval('public.competencia_competencia_id_seq'),
                Torneo_id INTEGER NOT NULL,
                Precio_base NUMERIC NOT NULL,
                Nombre VARCHAR NOT NULL,
                Cupo INTEGER NOT NULL,
                CONSTRAINT competencia_pk PRIMARY KEY (Competencia_id)
);


ALTER SEQUENCE public.competencia_competencia_id_seq OWNED BY public.Competencia.Competencia_id;

CREATE SEQUENCE public.inscripcion_inscripcion_id_seq;

CREATE TABLE public.Inscripcion (
                Inscripcion_id INTEGER NOT NULL DEFAULT nextval('public.inscripcion_inscripcion_id_seq'),
                Competencia_id INTEGER NOT NULL,
                Precio NUMERIC NOT NULL,
                Fecha DATE NOT NULL,
                Participante_id INTEGER NOT NULL,
                CONSTRAINT inscripcion_pk PRIMARY KEY (Inscripcion_id)
);


ALTER SEQUENCE public.inscripcion_inscripcion_id_seq OWNED BY public.Inscripcion.Inscripcion_id;

ALTER TABLE public.Torneo ADD CONSTRAINT administrador_torneo_fk
FOREIGN KEY (Administrador_id)
REFERENCES public.Administrador (Administrador_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Competencia ADD CONSTRAINT torneo_competencia_fk
FOREIGN KEY (Torneo_id)
REFERENCES public.Torneo (Torneo_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Inscripcion ADD CONSTRAINT participante_inscripcion_fk
FOREIGN KEY (Participante_id)
REFERENCES public.Participante (Participante_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Inscripcion ADD CONSTRAINT competencia_inscripcion_fk
FOREIGN KEY (Competencia_id)
REFERENCES public.Competencia (Competencia_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
