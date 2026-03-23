create schema if not exists profiles;

create table if not exists profiles.profiles (
    id serial primary key,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    role varchar(255) not null,
    identification_number varchar(255) not null,
    customer_type varchar(255) not null,
    citizenship varchar(255) not null,
    address varchar(255) not null,
    contact_number varchar(255) not null,
    dod date not null
);