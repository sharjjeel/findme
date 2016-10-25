drop table if exists items;
drop table if exists users;

create table users (
    id varchar(100) primary key,
    name varchar(100),
    password text
);

create table items (
    id varchar(100) primary key,
    name varchar(100),
    description text,
    timestamp varchar(100),
    longitude double precision,
    latitude double precision,
    claim_status varchar(100),
    user_id varchar(100) references users(id),
    lost boolean
);

create index user_id on users (id);
create index item_user on items (user_id);
create index item_claims on items (claim_status);
