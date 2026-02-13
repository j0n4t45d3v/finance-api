create table if not exists tb_transactions
(
    id             bigserial      not null,
    user_id        bigint         not null,
    category_id    bigint         not null,
    account_id     bigint         not null,
    amount         numeric(10, 2) not null,
    transaction_at timestamp      not null
);

alter table tb_transactions
    add constraint pk_transaction_id primary key (id);

alter table tb_transactions
    add constraint fk_transaction_user_id foreign key (user_id) references tb_users (id);

alter table tb_transactions
    add constraint fk_transaction_user_account_id foreign key (account_id) references tb_user_accounts (id);

alter table tb_transactions
    add constraint fk_transaction_category_id foreign key (category_id) references tb_categories (id);
