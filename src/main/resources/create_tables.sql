create table cities
(
    id   serial
        constraint cities_pk
            primary key,
    name varchar(30) not null
);

create table timezones
(
    id           serial
        constraint timezones_pk
            primary key,
    abbreviation varchar(30) not null,
    time_offset  varchar(6)
);

create unique index timezones_abbreviation_uindex
    on timezones (abbreviation);

create table cities_timezones
(
    id          serial
        constraint cities_timezones_pk
            primary key,
    city_id     integer not null
        constraint cities_timezones_city_id_to_cities_id_fk
            references cities
            on update restrict on delete set null,
    timezone_id integer
        constraint cities_timezones_timezone_id_to_timezones_id_fk
            references timezones
            on update restrict on delete set null,
    from_time   varchar(30),
    to_time     varchar(30)
);