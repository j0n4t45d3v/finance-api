package com.jonatas.finance.domain;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_user_accounts")
public class Account {

    public record Description(@Nonnull String value) {}

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

    public Account(@Nonnull Description description, @Nonnull User user, boolean main) {
        this.description = description;
        this.main = main;
        this.user = user;
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
