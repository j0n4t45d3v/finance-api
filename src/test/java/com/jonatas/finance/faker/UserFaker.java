package com.jonatas.finance.faker;

import java.util.concurrent.ThreadLocalRandom;

import com.jonatas.finance.auth.Email;
import com.jonatas.finance.auth.Password;
import com.jonatas.finance.auth.User;

public final class UserFaker extends Faker<User> {

    private Long id;
    private String email;
    private String password;
    
    protected UserFaker() {
        this.id = ThreadLocalRandom.current().nextLong();
        this.email = "john@doe.com";
        this.password = "secretPassword123@";
    }

    public UserFaker withId(Long id) {
        this.id = id;
        return this;
    }

    public UserFaker withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserFaker withPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public User get() {
        return new User(id, new Email(email), new Password(password));
    }

}
