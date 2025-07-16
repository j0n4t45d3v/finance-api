package br.com.managementfinanceapi.factory;

import br.com.managementfinanceapi.adapter.out.entity.transaction.BalanceEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BalanceFactory {
  @FunctionalInterface
  interface FakeDataBalance {
    BalanceEntity make(UserEntity user);
  }

  private static final int MAX_DATA_SIZE = 6;
  private static final List<FakeDataBalance> FAKE_DATA = new ArrayList<>(MAX_DATA_SIZE);

  static {
    FAKE_DATA.add((user) ->
        BalanceFactory.create( BigDecimal.valueOf(5000),YearMonth.of(2025, 1), user));

    FAKE_DATA.add((user) ->
        BalanceFactory.create( BigDecimal.valueOf(250), YearMonth.of(2025, 2), user));

    FAKE_DATA.add((user) ->
        BalanceFactory.create(BigDecimal.valueOf(1200), YearMonth.of(2025, 3), user));

    FAKE_DATA.add((user) ->
        BalanceFactory.create( BigDecimal.valueOf(300), YearMonth.of(2025, 4), user));

    FAKE_DATA.add((user) ->
        BalanceFactory.create( BigDecimal.valueOf(150), YearMonth.of(2025, 5), user));

    FAKE_DATA.add((user) ->
        BalanceFactory.create( BigDecimal.valueOf(80), YearMonth.of(2025, 6), user));
  }

  public static List<BalanceEntity> createFakeListData(int quantity) {
    UserEntity user = UserFactory.createFakeData();
    List<BalanceEntity> fakeData = new ArrayList<>(quantity);
    BalanceEntity data;
    while (quantity > 0) {
      data = BalanceFactory.createFakeData(user);
      if(fakeData.stream().map(BalanceEntity::getMonth).toList().contains(data.getMonth())) {
        continue;
      }
      fakeData.add(data);
      quantity--;
    }
    return fakeData;
  }
  public static BalanceEntity createFakeData() {
    return BalanceFactory.createFakeData(UserFactory.createFakeData());
  }

  public static BalanceEntity createFakeData(UserEntity user) {
    FakeDataBalance balance = FAKE_DATA.get(ThreadLocalRandom.current().nextInt(MAX_DATA_SIZE));
    return balance.make(user);
  }

  public static BalanceEntity create(
      BigDecimal amount,
      YearMonth yearMonth,
      UserEntity user
  ) {
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    System.out.println("Create Balance: ");
    System.out.printf("  Amount: R$ %.2f%n", amount.doubleValue());
    System.out.printf("  Date: %s%n", yearMonth.format(DateTimeFormatter.ofPattern("MM/yyyy")));
    System.out.printf("  User: %s%n", user.getEmail());
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    return new BalanceEntity(null, amount, user, (short) yearMonth.getMonthValue(), (short) yearMonth.getYear());
  }
}
