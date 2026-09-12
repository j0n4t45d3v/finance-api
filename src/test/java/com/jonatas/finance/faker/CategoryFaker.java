package com.jonatas.finance.faker;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.wallet.Category;

public final class CategoryFaker extends Faker<Category> {

    private Long id;
    private String name;
    private Category.Type type;
    private User user;

    public CategoryFaker() {
        this.id = numberLong();
        this.name = text(50);
        this.type = Category.Type.EXPENSE;
        this.user = Faker.user().get();
    }

    public CategoryFaker withId(Long id) {
        this.id = id;
        return this;
    }

    public CategoryFaker withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryFaker withType(Category.Type type) {
        this.type = type;
        return this;
    }

    public CategoryFaker withUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public Category get() {
        return new Category(this.id, getOrNull(this.name, Category.Name::of), this.type, this.user);
    }
}
