package com.jonatas.finance.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_user_accounts")
public class Account {

    public record Description(String value) {
        public Description {
            Objects.requireNonNull(value);
            if (value.isBlank()) {
                throw new IllegalArgumentException("description cannot be blank");
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "description"))
    private Description description;

    private boolean main;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Account() {}

    public Account(Description description, User user, boolean main) {
        this.description = Objects.requireNonNull(description);
        this.main = main;
        this.user = Objects.requireNonNull(user);
    }

    public Long getId() {
        return this.id;
    }

    public String getDescriptionValue() {
        return this.description.value();
    }

    public boolean isMain() {
        return this.main;
    }

    public User getUser() {
        return this.user;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}
