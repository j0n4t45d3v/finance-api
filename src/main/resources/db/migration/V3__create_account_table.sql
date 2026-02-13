create table if not exists tb_user_accounts
(
    id          bigserial   not null,
    user_id     bigint      not null,
    description varchar(30) not null,
    main        boolean not null default false
);


alter table tb_user_accounts
    add constraint pk_account_id primary key (id);

alter table tb_user_accounts
    add constraint fk_account_user_id foreign key (user_id) references tb_users (id);

alter table tb_user_accounts
    add constraint uk_account_description unique (user_id, description);
