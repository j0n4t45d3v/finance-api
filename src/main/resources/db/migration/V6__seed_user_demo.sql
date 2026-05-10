DO $$
BEGIN

-- =========================================
-- Usuário demo
-- =========================================
    
    insert into tb_users("email", "password")
    values ('john@doe.example', '$2a$10$b3ZZV2o57xCkWVCF1Jg/ruoMQbRJ1yd4CwBNiP28lWFrNfh9EbKBG')
    on conflict do nothing;

-- =========================================
-- Categorias
-- =========================================

    insert into tb_categories("name", "type", "user_id")
    select *
    from (
        values
            ('Salário', 'INCOME'),
            ('Freelance', 'INCOME'),
            ('Investimentos', 'INCOME'),

            ('Alimentação', 'EXPENSE'),
            ('Transporte', 'EXPENSE'),
            ('Moradia', 'EXPENSE'),
            ('Streaming', 'EXPENSE'),
            ('Lazer', 'EXPENSE'),
            ('Saúde', 'EXPENSE'),
            ('Educação', 'EXPENSE')
    ) as categories("name", "type")
    cross join (
        select id as user_id
        from tb_users
        where email = 'john@doe.example'
    ) u
    on conflict do nothing;


-- =========================================
-- Contas bancárias
-- =========================================

    insert into tb_user_accounts("user_id", "description", "main")
    select u.id, accounts.description, accounts.main
    from (
        values
            ('Conta Principal', true),
            ('Carteira', false),
            ('Reserva de Emergência', false),
            ('Cartão Virtual', false)
    ) as accounts("description", "main")
    cross join (
        select id
        from tb_users
        where email = 'john@doe.example'
    ) u
    on conflict do nothing;


-- =========================================
-- Transações
-- =========================================

    insert into tb_transactions (
        "user_id",
        "category_id",
        "account_id",
        "amount",
        "transaction_at",
        "description"
    )
    select
        u.id,
        c.id,
        a.id,
        t.amount,
        t.transaction_at,
        t.description
    from (
        values

        -- Receitas

        ('Salário', 'Conta Principal', 5200.00, timestamp '2025-05-05 08:00:00', 'Salário empresa'),
        ('Freelance', 'Conta Principal', 850.00, timestamp '2025-05-12 19:30:00', 'Projeto landing page'),
        ('Investimentos', 'Reserva de Emergência', 120.00, timestamp '2025-05-18 10:00:00', 'Rendimento CDI'),

        -- Alimentação

        ('Alimentação', 'Conta Principal', 42.50, timestamp '2025-05-03 12:10:00', 'Almoço restaurante'),
        ('Alimentação', 'Conta Principal', 18.90, timestamp '2025-05-04 09:00:00', 'Padaria'),
        ('Alimentação', 'Conta Principal', 95.20, timestamp '2025-05-09 20:15:00', 'Supermercado'),

        -- Transporte

        ('Transporte', 'Conta Principal', 22.30, timestamp'2025-05-06 08:00:00', 'Uber trabalho'),
        ('Transporte', 'Conta Principal', 12.00, timestamp'2025-05-07 18:30:00', 'Ônibus'),

        -- Moradia

        ('Moradia', 'Conta Principal', 1450.00, timestamp '2025-05-01 09:00:00', 'Aluguel'),
        ('Moradia', 'Conta Principal', 180.00, timestamp '2025-05-10 11:00:00', 'Conta de energia'),
        ('Moradia', 'Conta Principal', 95.00, timestamp '2025-05-11 14:00:00', 'Conta de internet'),

        -- Streaming

        ('Streaming', 'Cartão Virtual', 39.90, timestamp '2025-05-08 21:00:00', 'Netflix'),
        ('Streaming', 'Cartão Virtual', 19.90, timestamp '2025-05-08 21:10:00', 'Spotify'),

        -- Saúde

        ('Saúde', 'Conta Principal', 75.00, timestamp '2025-05-13 16:00:00', 'Farmácia'),

        -- Educação

        ('Educação', 'Conta Principal', 89.90, timestamp '2025-05-15 20:00:00', 'Curso Java'),

        -- Lazer

        ('Lazer', 'Carteira', 120.00, timestamp '2025-05-17 22:00:00', 'Cinema e jantar')

    ) as t("category_name", "account_name", "amount", "transaction_at", "description")

    join tb_users u
        on u.email = 'john@doe.example'

    join tb_categories c
        on c.name = t.category_name
    and c.user_id = u.id

    join tb_user_accounts a
        on a.description = t.account_name
    and a.user_id = u.id;

END $$
