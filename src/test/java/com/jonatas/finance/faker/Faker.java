package com.jonatas.finance.faker;

import java.math.BigDecimal;
import java.time.Instant;
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

    public static TransactionFaker transaction() {
        return new TransactionFaker();
    }

    public static String email() {
        return text(10) + "@example.com";
    }

    public static String options(String... options) {
        int index = numberInt(0, options.length);
        return options[index % options.length];
    }

    public static String text(int length) {
        var builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append((char) ('a' + numberInt(0, 26)));
        }
        return builder.toString();
    }

    public static Instant instant() {
        return Instant.ofEpochMilli(numberLong(0, Instant.now().toEpochMilli()));
    }

    public static int numberInt() {
        return numberInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int numberInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static long numberLong() {
        return numberLong(1, Long.MAX_VALUE);
    }

    public static long numberLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public static double numberDouble() {
        return numberDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static BigDecimal numberBigDecimal(double min, double max) {
        return BigDecimal.valueOf(numberDouble(min, max));
    }

    public static double numberDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    protected <T, R> R getOrNull(T value, Function<T, R> handle) {
        return Objects.isNull(value) ? null : handle.apply(value);
    }

    public abstract R get();
}
