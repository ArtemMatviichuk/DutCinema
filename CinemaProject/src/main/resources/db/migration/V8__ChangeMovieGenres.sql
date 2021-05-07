ALTER TABLE movie_genres DROP FOREIGN KEY movie_genres_genres_fk;
alter table movie_genres
add constraint movie_genres_genres_fk
foreign key (genre_id) references genres (id)
ON DELETE CASCADE;