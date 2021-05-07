create table halls (
                       id bigint not null,
                       name varchar(255),
                       places integer,
                       rows_amount integer,
                       primary key (id));

create table hibernate_sequence (
    next_val bigint);

insert into hibernate_sequence values ( 1 );

create table movies (
                        id bigint not null,
                        description varchar(255),
                        duration integer, poster_path varchar(255),
                        title varchar(255),
                        trailer_path varchar(255),
                        primary key (id));

create table reviews (
                         id bigint not null,
                         creation_date datetime(6),
                         text varchar(255),
                         user_id bigint not null,
                         movie_id bigint not null,
                         primary key (id));

create table sessions (
                          id bigint not null,
                          date datetime(6),
                          hall_id bigint not null,
                          movie_id bigint not null,
                          primary key (id));

create table tickets (
                         id bigint not null,
                         place integer,
                         row integer,
                         user_id bigint not null,
                         session_id bigint not null,
                         primary key (id));

create table users (
                       id bigint not null,
                       email varchar(255),
                       name varchar(255) not null,
                       password varchar(255) not null,
                       primary key (id));

alter table reviews
    add constraint reviews_user_fk
        foreign key (user_id) references users (id);

alter table reviews
    add constraint reviews_movie_id
        foreign key (movie_id) references movies (id);

alter table sessions
    add constraint sessions_hall_id
        foreign key (hall_id) references halls (id);

alter table sessions
    add constraint sessions_movie_id
        foreign key (movie_id) references movies (id);
alter table tickets

    add constraint tickets_user_fk
        foreign key (user_id) references users (id);

alter table tickets
    add constraint tickets_session_id
        foreign key (session_id) references sessions (id);