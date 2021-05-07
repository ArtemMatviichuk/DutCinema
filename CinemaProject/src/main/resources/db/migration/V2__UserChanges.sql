ALTER TABLE users DROP COLUMN name;
ALTER TABLE users ADD first_name varchar(255) not null;
ALTER TABLE users ADD last_name varchar(255) not null;
ALTER TABLE users ADD created datetime(6) null;
ALTER TABLE users ADD updated datetime(6) null;
ALTER TABLE users ADD status varchar(255) null;
create table roles (
                         id bigint not null,
                         name varchar(255) not null,
                         primary key (id));
create table user_roles (
                       user_id bigint not null,
                       role_id bigint not null,
                       primary key (user_id, role_id));
alter table user_roles
    add constraint user_roles_users_fk
        foreign key (user_id) references users (id);
alter table user_roles
    add constraint user_roles_roles_fk
        foreign key (role_id) references roles (id);