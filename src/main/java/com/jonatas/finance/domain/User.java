package com.jonatas.finance.domain;

import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.dvo.user.Password;
import com.jonatas.finance.domain.exception.FieldRequiredException;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;


@Entity
@Table(name= "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride( name = "value", column = @Column(name="email"))
    private Email email;

    @Embedded
    @AttributeOverride( name = "value", column = @Column(name="password"))
    private Password password;

    protected User() {
    }

    private User(Long id) {
      this.id = id;
    }

    public User(Email email, Password password) {
        if (password == null) {
            throw new FieldRequiredException("password");
        }

        if (email == null) {
            throw new FieldRequiredException("email");
        }
        this.email = email;
        this.password = password;
    }

    public static User reference(@Nonnull Long userId) {
      return new User(userId);
    }

    public Long getId() {
        return id;
    }

    public String getPasswordValue() {
        return this.password.value();
    }

    public Email getEmail() {
        return email;
    }

    public String getEmailValue() {
        return this.email.value();
    }
}
