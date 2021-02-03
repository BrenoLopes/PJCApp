create table users (
    id serial8 primary key,
    email varchar(255) not null unique,
    name varchar(255) not null,
    password varchar(255) not null
);

create table artists (
    id serial8 primary key,
    name varchar(255) not null,
    author serial8 not null,
    foreign key (author) references users(id)
);

create table albums (
    id serial8 primary key,
    name varchar(255) not null,
    image varchar(255),
    artist_id int8,
    foreign key (artist_id) references artists(id)
);

-- insert into artists(name)
--     values ('Serj tankian'),
--            ('Mike Shinoda'),
--            ('Michel Teló'),
--            ('Guns N'' Roses');
--
-- insert into albums(name, image, artist_id)
--     values ('Harakiri','',(select id from Artists WHERE name='Serj tankian')),
--            ('Black Blooms','',(select id from Artists WHERE name='Serj tankian')),
--            ('The Rough Dog','',(select id from Artists WHERE name='Serj tankian'));
--
-- insert into albums(name, image, artist_id)
--     values ('The Rising Tied','',(select id from Artists WHERE name='Mike Shinoda')),
--            ('Post Traumatic','',(select id from Artists WHERE name='Mike Shinoda')),
--            ('Post Traumatic EP','',(select id from Artists WHERE name='Mike Shinoda')),
--            ('Where''d You Go','',(select id from Artists WHERE name='Mike Shinoda'));
--
-- insert into albums(name, image, artist_id)
--     values ('Bem Sertanejo','',(select id from Artists WHERE name='Michel Teló')),
--            ('Bem Sertanejo - O Show (Ao Vivo)','',(select id from Artists WHERE name='Michel Teló')),
--            ('Bem Sertanejo - (1a Temporada) - EP','',(select id from Artists WHERE name='Michel Teló'));
--
-- insert into albums(name, image, artist_id)
--     values ('Use Your IIIlusion I','',(select id from Artists WHERE name='Guns N'' Roses')),
--            ('Use Your IIIlusion II','',(select id from Artists WHERE name='Guns N'' Roses')),
--            ('Greatest Hits','',(select id from Artists WHERE name='Guns N'' Roses'));