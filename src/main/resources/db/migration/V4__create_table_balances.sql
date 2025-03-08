create table if not exists balances
(
  id      bigserial      not null,
  user_id bigint         not null,
  amount  numeric(20, 2) not null,
  mouth   smallint       not null,
  year    smallint       not null default extract(year from now())
);

alter table balances
  add constraint pk_balance primary key (id);

alter table balances
  add constraint fk_balance_user foreign key (user_id) references users(id);
