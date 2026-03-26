create schema if not exists profiles;

create table if not exists profiles.roles (
    id serial primary key,
    role varchar(255) not null,
    code varchar(255) not null
);

INSERT INTO  profiles.roles (role, code)
VALUES
    ('Admin', 1),
    ('Customer', 2),
    ('Guest', 3);
