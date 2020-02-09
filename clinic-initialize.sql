
CREATE TABLE public.branches
(
    id integer NOT NULL DEFAULT nextval('branches_id_seq'::regclass),
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    phonenumber character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT branches_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.branches
    OWNER to postgres;

CREATE TABLE public.customers
(
    id integer NOT NULL DEFAULT nextval('customers_id_seq'::regclass),
    firstname character varying(255) COLLATE pg_catalog."default",
    isadmin integer,
    lastname character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    secondname character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    birthday date,
    phone_number character varying(255) COLLATE pg_catalog."default",
    social_status character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT customers_pkey PRIMARY KEY (id),
    CONSTRAINT uk_bepynu3b6l8k2ppuq6b33xfxc UNIQUE (username)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.customers
    OWNER to postgres;


CREATE TABLE public.doctors
(
    id integer NOT NULL DEFAULT nextval('doctors_id_seq'::regclass),
    firstname character varying(255) COLLATE pg_catalog."default",
    isadmin integer,
    lastname character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    secondname character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    "position" character varying(255) COLLATE pg_catalog."default",
    specialization character varying(255) COLLATE pg_catalog."default",
    branch_id integer,
    CONSTRAINT doctors_pkey PRIMARY KEY (id),
    CONSTRAINT uk_15xrlsp4drthssb0ns4pghcfk UNIQUE (username)
,
    CONSTRAINT fk3bgp2drsondl5eoggyehcy2y0 FOREIGN KEY (branch_id)
        REFERENCES public.branches (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.doctors
    OWNER to postgres;

CREATE TABLE public.visits
(
    id integer NOT NULL DEFAULT nextval('visits_id_seq'::regclass),
    date timestamp without time zone,
    diagnosis character varying(255) COLLATE pg_catalog."default",
    customer_id integer,
    doctor_id integer,
    CONSTRAINT visits_pkey PRIMARY KEY (id),
    CONSTRAINT fkqi3oh1lhfuae6hgu52joagnxt FOREIGN KEY (customer_id)
        REFERENCES public.customers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fkth95fndjk3y3nepjfu3f66r63 FOREIGN KEY (doctor_id)
        REFERENCES public.doctors (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.visits
    OWNER to postgres;


INSERT INTO public.branches(
	id, description, name, phonenumber)
	VALUES (11, 'Приёмное', '+375296645789', 'Постановка первичного диагноза'),
	(12,'Хирургическое', '+375898744563','Оказание хирургической помощи'),
	(13,'Терапевтическое','+345699764866','Лечение терапевтических заболеваний'),
	(14,'Онкологическое','+37533479665','Занимается профессиональным лечением онкологических заболеваний'),
	(15,'Гинекологическое','+375297245896','Принимает пациаентов с гинекологическими заболеваниями');

INSERT INTO public.customers(
	id, firstname, isadmin, lastname, password, secondname, username, birthday, phone_number, social_status)
	VALUES (5, 'Вадим', '0', 'Витальевич', 'password123', 'Керсновский','kersnovskiy123', '1999-12-10', '+375266644457', 'Студент'),
	(6, 'Артём', '0', 'Викторович', 'passforartem213', 'Берестень', 'berestenlogin222', '1999-10-10', '+332147775566', 'Студент'),
	(7, 'Виталий', '0', 'Степанович', 'Stepanovich999', 'Грицкевич', 'Griskeviclogin', '2000-11-11', '+375298874456', 'Рабочий'),
	(8, 'Галина', '0', 'Максимовна', 'Maksimovna999', 'Столярова', 'Stolyarova129', '1976-10-10', '+375298774453', 'Рабочий'),
	(9, 'Дарья', '0', 'Денисовна', 'Denisovnananana', 'Дембель', 'Dembel777726', '1999-10-11', '+375336665566', 'Студент'),
	(10, 'Виктор', '0', 'Витальевич', 'Vitalievich992', 'Боевой', 'Boymaloy123', '1967-01-02', '+375996544475', 'Пенсионер');

INSERT INTO public.doctors(
	id, firstname, isadmin, lastname, password, secondname, username, "position", specialization, branch_id)
	VALUES (15, 'Анатолий', 0, 'Григорьевич', 'loginpassword12345', 'Достоевский', 'usernamepassword', 'главрач', 'хирург', 12),
	(16,'Виктор',0,'Дмитриевич', 'vikktorpasswords','Долгобродский','dolgobr1244','врач-санитар','гинеколог',15),
	(17,'Ульяна',0,'Анатольевна','ylaydovart222','Смирная','smirnayatakaya','врач-акушер','терапевт',13),
	(18,'Мария',0,'Германовна','germandavayd','Скользкий','SkolzkiyTip','врач','онколошг',14);

INSERT INTO public.visits(
	id, date, diagnosis, customer_id, doctor_id)
	VALUES (30, '2020-01-01 9:20', 'Не установлен', 6, 15)
	(31, '2020-01-01 11:40', 'Не установлен', null, 15),
	(32, '2020-02-05 18:00', 'Не установлен', null, 16),
	(33, '2020-01-20 19:30', 'Не установлен', null, 17),
	(34, '2020-04-18 11:10', 'Не установлен', null, 18),
	(35, '2020-02-01 12:35', 'Не установлен', null, 16),
	(36, '2020-01-09 17:40', 'Не установлен', null, 17),
	(37, '2020-01-09 22:35', 'Не установлен', null, 18),
	(38, '2020-01-05 8:10', 'Не установлен', null, 19),
	(39, '2020-01-03 11:10', 'Не установлен', null, 16),
	(40, '2020-01-04 12:15', 'Не установлен', null, 17);