create table if not exists users
(
  id         bigserial    not null,
  email      varchar(255) not null unique,
  password   varchar(255) not null,
  created_at timestamp    not null default current_timestamp,
  updated_at timestamp    not null default current_timestamp
);

alter table users
  add constraint pk_user primary key (id);

create table if not exists categories
(
  id         bigserial   not null,
  name       varchar(20) not null,
  user_id    bigint      not null,
  created_at timestamp   not null default current_timestamp,
  updated_at timestamp   not null default current_timestamp
);

alter table categories
  add constraint pk_category primary key (id);

alter table categories
  add constraint fk_category_user foreign key (user_id) references users (id);

create table if not exists transactions
(
  id               bigserial      not null,
  amount           decimal(15, 2) not null,
  description      varchar(255),
  type             char(1)        not null default 'E',
  date_transaction timestamp      not null default now(),
  user_id          bigint         not null,
  category_id      bigint         not null,
  created_at       timestamp      not null default current_timestamp,
  updated_at       timestamp      not null default current_timestamp
);

alter table transactions
  add constraint pk_transaction primary key (id);

alter table transactions
  add constraint chk_transaction_type check ( type in ('I', 'E') );

alter table transactions
  add constraint fk_transaction_user foreign key (user_id) references users (id);

alter table transactions
  add constraint fk_transaction_category foreign key (category_id) references categories (id);
