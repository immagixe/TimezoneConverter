create table cities
(
    id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name varchar(30) not null
);

create table timezones
(
    id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    abbreviation varchar(30) not null,
    time_offset  varchar(6)
);

create table cities_timezones
(
    id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    city_id     integer not null,
    timezone_id integer not null,
    from_time   varchar(30),
    to_time     varchar(30),
    FOREIGN KEY (city_id)  REFERENCES cities (id),
    FOREIGN KEY (timezone_id)  REFERENCES timezones (id)
);