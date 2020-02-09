
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
	VALUES (11, '�������', '+375296645789', '���������� ���������� ��������'),
	(12,'�������������', '+375898744563','�������� ������������� ������'),
	(13,'���������������','+345699764866','������� ��������������� �����������'),
	(14,'��������������','+37533479665','���������� ���������������� �������� �������������� �����������'),
	(15,'����������������','+375297245896','��������� ���������� � ����������������� �������������');

INSERT INTO public.customers(
	id, firstname, isadmin, lastname, password, secondname, username, birthday, phone_number, social_status)
	VALUES (5, '�����', '0', '����������', 'password123', '�����������','kersnovskiy123', '1999-12-10', '+375266644457', '�������'),
	(6, '����', '0', '����������', 'passforartem213', '���������', 'berestenlogin222', '1999-10-10', '+332147775566', '�������'),
	(7, '�������', '0', '����������', 'Stepanovich999', '���������', 'Griskeviclogin', '2000-11-11', '+375298874456', '�������'),
	(8, '������', '0', '����������', 'Maksimovna999', '���������', 'Stolyarova129', '1976-10-10', '+375298774453', '�������'),
	(9, '�����', '0', '���������', 'Denisovnananana', '�������', 'Dembel777726', '1999-10-11', '+375336665566', '�������'),
	(10, '������', '0', '����������', 'Vitalievich992', '������', 'Boymaloy123', '1967-01-02', '+375996544475', '���������');

INSERT INTO public.doctors(
	id, firstname, isadmin, lastname, password, secondname, username, "position", specialization, branch_id)
	VALUES (15, '��������', 0, '�����������', 'loginpassword12345', '�����������', 'usernamepassword', '�������', '������', 12),
	(16,'������',0,'����������', 'vikktorpasswords','�������������','dolgobr1244','����-�������','���������',15),
	(17,'������',0,'�����������','ylaydovart222','�������','smirnayatakaya','����-������','��������',13),
	(18,'�����',0,'����������','germandavayd','���������','SkolzkiyTip','����','��������',14);

INSERT INTO public.visits(
	id, date, diagnosis, customer_id, doctor_id)
	VALUES (30, '2020-01-01 9:20', '�� ����������', 6, 15)
	(31, '2020-01-01 11:40', '�� ����������', null, 15),
	(32, '2020-02-05 18:00', '�� ����������', null, 16),
	(33, '2020-01-20 19:30', '�� ����������', null, 17),
	(34, '2020-04-18 11:10', '�� ����������', null, 18),
	(35, '2020-02-01 12:35', '�� ����������', null, 16),
	(36, '2020-01-09 17:40', '�� ����������', null, 17),
	(37, '2020-01-09 22:35', '�� ����������', null, 18),
	(38, '2020-01-05 8:10', '�� ����������', null, 19),
	(39, '2020-01-03 11:10', '�� ����������', null, 16),
	(40, '2020-01-04 12:15', '�� ����������', null, 17);