package com.jonatas.finance.faker;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.wallet.Wallet;
import java.util.concurrent.ThreadLocalRandom;

public final class WalletFaker extends Faker<Wallet> {

    private Long id;
    private String description;
    private boolean main;
    private User user;

    WalletFaker() {
        this.id = ThreadLocalRandom.current().nextLong();
        this.description = "Test Wallet";
        this.main = false;
        this.user = user().get();
    }

    public WalletFaker withId(Long id) {
        this.id = id;
        return this;
    }

    public WalletFaker withDescription(String description) {
        this.description = description;
        return this;
    }

    public WalletFaker withUser(User user) {
        this.user = user;
        return this;
    }

    public WalletFaker isMainWallet() {
        return this.withMain(true);
    }

    public WalletFaker isNotMainWallet() {
        return this.withMain(false);
    }

    public WalletFaker withMain(boolean isMain) {
        this.main = isMain;
        return this;
    }

    @Override
    public Wallet get() {
        return new Wallet(id, new Wallet.Description(description), user, main);
    }
}
