create table Artists (
    id serial8 not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table Albums (
    id serial8 not null,
    name varchar(255) not null unique,
    image varchar(255),
    artist_id int8,
    primary key(id),
    foreign key (artist_id) references Artists(id)
);

create table Users (
    id serial8 not null,
    email varchar(255) not null unique,
    name varchar(255) not null,
    password varchar(255) not null,
    primary key(id)
);

create sequence public.hibernate_sequence;

insert into Artists(name)
    values ('Serj tankian'),
           ('Mike Shinoda'),
           ('Michel Teló'),
           ('Guns N'' Roses');

insert into Albums(name, image, artist_id)
    values ('Harakiri','',(select id from Artists WHERE name='Serj tankian')),
           ('Black Blooms','',(select id from Artists WHERE name='Serj tankian')),
           ('The Rough Dog','',(select id from Artists WHERE name='Serj tankian'));

insert into Albums(name, image, artist_id)
    values ('The Rising Tied','',(select id from Artists WHERE name='Mike Shinoda')),
           ('Post Traumatic','',(select id from Artists WHERE name='Mike Shinoda')),
           ('Post Traumatic EP','',(select id from Artists WHERE name='Mike Shinoda')),
           ('Where''d You Go','',(select id from Artists WHERE name='Mike Shinoda'));

insert into Albums(name, image, artist_id)
    values ('Bem Sertanejo','',(select id from Artists WHERE name='Michel Teló')),
           ('Bem Sertanejo - O Show (Ao Vivo)','',(select id from Artists WHERE name='Michel Teló')),
           ('Bem Sertanejo - (1a Temporada) - EP','',(select id from Artists WHERE name='Michel Teló'));

insert into Albums(name, image, artist_id)
    values ('Use Your IIIlusion I','',(select id from Artists WHERE name='Guns N'' Roses')),
           ('Use Your IIIlusion II','',(select id from Artists WHERE name='Guns N'' Roses')),
           ('Greatest Hits','',(select id from Artists WHERE name='Guns N'' Roses'));