package com.jonatas.finance.faker;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public abstract class Faker<R> {

  public static UserFaker user() {
    return new UserFaker();
  }

  public static WalletFaker wallet() {
    return new WalletFaker();
  }

  public static CategoryFaker category() {
    return new CategoryFaker();
  }

  public static String email() {
    return text(10) + "@example.com";
  }

  public static String text(int length) {
    var builder = new StringBuilder();
    for (int i = 0; i < length; i++) {
      builder.append((char) ('a' + numberInt(0, 26)));
    }
    return builder.toString();
  }

  public static int numberInt() {
    return numberInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public static int numberInt(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max);
  }

  public static long numberLong() {
    return ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
  }

  public static double numberDouble() {
    return numberDouble(Double.MIN_VALUE, Double.MAX_VALUE);
  }

  public static double numberDouble(double min, double max) {
    return ThreadLocalRandom.current().nextDouble(min, max);
  }

  protected <T, R> R getOrNull(T value, Function<T, R> handle) {
    return Objects.isNull(value) ? null : handle.apply(value);
  }

  public abstract R get();
}
