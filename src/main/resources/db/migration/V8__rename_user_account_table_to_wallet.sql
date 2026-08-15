alter table tb_user_accounts rename to tb_wallets;

alter table tb_wallets
    rename constraint pk_account_id to pk_wallet_id;

alter table tb_wallets
    rename constraint fk_account_user_id to fk_wallet_user_id;

alter table tb_wallets
    rename constraint uk_account_description to uk_wallet_description;

alter table tb_transactions
    rename column account_id to wallet_id;

alter table tb_transactions
    rename constraint fk_transaction_user_account_id to fk_transaction_wallet_id;
