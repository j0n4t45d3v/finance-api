alter table tb_categories
    drop constraint uk_category_name;

alter table tb_categories
    add constraint uk_category_name unique(user_id, name);
