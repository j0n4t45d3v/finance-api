package com.jonatas.finance.domain;

import com.jonatas.finance.domain.exception.DomainException;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Table(name = "tb_transactions")
public class Transaction {

    public record Description(String value) {
        public Description {
            if (value == null || value.isBlank()) {
                value = "<without description>";
            }
        }

        public static Description empty() {
            return new Description(null);
        }
    }

    public record Amount(@Nonnull BigDecimal value) {
        public Amount {
            if (value.doubleValue() <= 0) {
                throw new DomainException("amount can not be less that zero");
            }
        }
    }

    public record Timestamp(@Nonnull LocalDateTime value) {
        public static Timestamp now() {
            return new Timestamp(LocalDateTime.now(ZoneId.of("UTC")));
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "description"))
    private Description description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "amount"))
    private Amount amount;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "transaction_at"))
    private Timestamp transactionAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    protected Transaction() {}

    public Transaction(
        Description description,
        Amount amount,
        Timestamp transactionAt,
        Account account,
        User user,
        Category category
    ) {
        this.description = Objects.requireNonNullElse(description, Description.empty());
        this.amount = Objects.requireNonNull(amount, "amount is required");
        this.user = Objects.requireNonNull(user, "user is required");
        this.transactionAt = Objects.requireNonNull(transactionAt, "transactionAt is required");
        this.account = Objects.requireNonNull(account, "account is required");
        this.category = Objects.requireNonNull(category, "category is required");
    }

    public Long getId() {
        return id;
    }

    public String getDescriptionValue() {
        return description.value();
    }

    public Amount getAmount() {
        return amount;
    }

    public BigDecimal getAmountValue() {
        return amount.value();
    }

    public Timestamp getTransactionAt() {
        return transactionAt;
    }

    public LocalDateTime getTransactionAtValue() {
        return this.transactionAt.value();
    }

    public Account getAccount() {
        return account;
    }

    public User getUser() {
        return user;
    }

    public Category getCategory() {
        return category;
    }

    public Category.Type getType() {
        return this.category.getType();
    }

    public Long getAccountId() {
        return this.account.getId();
    }
}
