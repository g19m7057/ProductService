create schema if not exists profiles;

create table if not exists profiles.profiles (
    id serial primary key,
    email varchar(255) not null,
    name char(100) not null,
    password varchar(255) not null,
    role varchar(2) not null,
    identification_number varchar(255) not null,
    customer_type int not null,
    country_code char(2) not null,
    marital_status VARCHAR(20),
    address varchar(255) not null,
    contact_number varchar(255) not null,
    dob date not null
);