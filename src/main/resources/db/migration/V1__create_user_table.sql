create table if not exists tb_users
(
    id       bigserial   not null,
    email    varchar(50) not null,
    password varchar(60)
);

alter table tb_users
    add constraint pk_user_id primary key (id);

alter table tb_users
    add constraint uk_user_email unique (email);
