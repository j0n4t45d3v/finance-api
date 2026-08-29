package com.jonatas.finance.faker;

public abstract class Faker<T extends Faker<T, R>, R> {

    public static UserFaker user() {
        return new UserFaker();
    }

    public abstract R get();

}
