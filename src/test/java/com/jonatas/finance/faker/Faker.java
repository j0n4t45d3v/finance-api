package com.jonatas.finance.faker;

public abstract class Faker<R> {

  public static UserFaker user() {
    return new UserFaker();
  }

  public static WalletFaker wallet() {
    return new WalletFaker();
  }

  public abstract R get();
}
