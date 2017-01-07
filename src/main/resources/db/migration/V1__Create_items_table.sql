create table items (
    id varchar(100) primary key,
    title text,
    description text,
    timestamp varchar(100),
    longitude double precision,
    latitude double precision,
    contact text,
    lost boolean
);