drop table if exists client;
drop table if exists address;
drop table if exists phone;
drop table if exists users;

create table client
(
    id         bigserial not null primary key,
    name       varchar(50),
    address_id bigint
);

create table address
(
    id        bigserial not null primary key,
    street    varchar(50),
    client_id bigint references client (id)
);

create table phone
(
    id        bigserial not null primary key,
    number    varchar(50),
    client_id bigint references client (id)
);

alter table client
    add constraint fk_address
        foreign key (address_id)
            references address (id);

create table users
(
    id       bigserial not null primary key,
    login    varchar(50) unique,
    password varchar(50),
    role varchar(20)
);

