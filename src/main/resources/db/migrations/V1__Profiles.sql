create schema if not exists profiles;

create table if not exists profiles.profiles (
    id serial primary key,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    role varchar(255) not null
);