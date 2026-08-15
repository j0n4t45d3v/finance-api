alter table tb_categories
    alter constraint uk_category_name unique(user_id, name);
