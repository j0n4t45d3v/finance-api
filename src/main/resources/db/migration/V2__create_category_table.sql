create table if not exists tb_categories (
  id bigserial not null,
  name varchar(50) not null,
  type varchar(7) not null,
  user_id bigint not null
);

alter table tb_categories
    add constraint pk_category_id primary key (id);

alter table tb_categories
    add constraint fk_category_user_id foreign key(user_id) references tb_users(id);

alter table tb_categories
    add constraint uk_category_name unique(name);

alter table tb_categories
    add constraint chk_category_type check(type in ('EXPENSE', 'INCOME')) ;


