ALTER TABLE movies DROP COLUMN genres;
create table genres (
    id bigint not null,
    name varchar(255) not null,
    primary key (id));
create table movie_genres (
    movie_id bigint not null,
    genre_id bigint not null,
    primary key (movie_id, genre_id));
alter table movie_genres
add constraint movie_genres_movies_fk
foreign key (movie_id) references movies (id);
alter table movie_genres
add constraint movie_genres_genres_fk
foreign key (genre_id) references genres (id);