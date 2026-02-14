package com.jonatas.finance.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


import com.jonatas.finance.domain.exception.DomainException;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_transactions")
public class Transaction {


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
        @Nonnull Amount amount,
        @Nonnull Timestamp transactionAt,
        @Nonnull Account account,
        @Nonnull User user,
        @Nonnull Category category
    ) {
        this.amount = amount;
        this.user = user;
        this.transactionAt = transactionAt;
        this.account = account;
        this.category = category;
    }

    public Long getId() {
        return id;
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
