package com.jonatas.finance.faker;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.wallet.Category;
import com.jonatas.finance.wallet.Transaction;
import com.jonatas.finance.wallet.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TransactionFaker extends Faker<Transaction> {

    private Long id;

    private String description;
    private BigDecimal amount;
    private LocalDateTime transactionAt;
    private User user;
    private Category category;
    private Wallet wallet;

    TransactionFaker() {
        this.id = numberLong();
        this.description = text(100);
        this.amount = numberBigDecimal(1, 10_00);
        this.transactionAt = LocalDateTime.now();
        this.user = user().get();
        this.category = category().get();
        this.wallet = wallet().get();
    }

    public TransactionFaker withId(Long id) {
        this.id = id;
        return this;
    }

    public TransactionFaker withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransactionFaker withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionFaker withTransactionAt(LocalDateTime transactionAt) {
        this.transactionAt = transactionAt;
        return this;
    }

    public TransactionFaker withUser(User user) {
        this.user = user;
        return this;
    }

    public TransactionFaker withCategory(Category category) {
        this.category = category;
        return this;
    }

    public TransactionFaker withWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    @Override
    public Transaction get() {
        return new Transaction(id,
                               getOrNull(description, Transaction.Description::new),
                               getOrNull(amount, Transaction.Amount::of),
                               getOrNull(transactionAt, Transaction.Timestamp::new),
                               wallet,
                               user,
                               category);
    }
}
