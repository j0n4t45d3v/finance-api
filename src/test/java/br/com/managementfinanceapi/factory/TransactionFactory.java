package br.com.managementfinanceapi.factory;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionFactory {

  @FunctionalInterface
  interface FakeDataTransaction {
    TransactionEntity make(UserEntity user, CategoryEntity category);
  }

  private static final int MAX_DATA_SIZE = 6;
  private static final List<FakeDataTransaction> FAKE_DATA = new ArrayList<>(MAX_DATA_SIZE);

  static {
    FAKE_DATA.add((user, category) ->
        TransactionFactory.create("Salary", BigDecimal.valueOf(5000), TransactionType.INCOME, LocalDateTime.now().minusDays(5), user, category));

    FAKE_DATA.add((user, category) ->
        TransactionFactory.create("Groceries", BigDecimal.valueOf(250), TransactionType.EXPENSE, LocalDateTime.now().minusDays(2), user, category));

    FAKE_DATA.add((user, category) ->
        TransactionFactory.create("Freelance", BigDecimal.valueOf(1200), TransactionType.INCOME, LocalDateTime.now().minusWeeks(1), user, category));

    FAKE_DATA.add((user, category) ->
        TransactionFactory.create("Electric Bill", BigDecimal.valueOf(300), TransactionType.EXPENSE, LocalDateTime.now().minusDays(10), user, category));

    FAKE_DATA.add((user, category) ->
        TransactionFactory.create("Book Sale", BigDecimal.valueOf(150), TransactionType.INCOME, LocalDateTime.now().minusMonths(1), user, category));

    FAKE_DATA.add((user, category) ->
        TransactionFactory.create("Restaurant", BigDecimal.valueOf(80), TransactionType.EXPENSE, LocalDateTime.now().minusHours(12), user, category));
  }

  public static TransactionEntity createFakeData(){
    UserEntity userFakeData = UserFactory.createFakeData();
    return TransactionFactory.createFakeData(userFakeData, CategoryFactory.createFakeData(userFakeData));
  }
  
  public static TransactionEntity createFakeData(UserEntity user, CategoryEntity category){
    FakeDataTransaction transaction = FAKE_DATA.get(ThreadLocalRandom.current().nextInt(MAX_DATA_SIZE));
    return transaction.make(user, category);
  }

  public static TransactionEntity create(
      String description,
      BigDecimal amount,
      TransactionType type,
      LocalDateTime date,
      UserEntity user,
      CategoryEntity category
  ){
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    System.out.println("Create Transaction: ");
    System.out.printf("  Description: %s%n", description);
    System.out.printf("  Amount: R$ %.2f%n", amount.doubleValue());
    System.out.printf("  Type: %s%n", type.name());
    System.out.printf("  Date: %s%n", date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
    System.out.printf("  User: %s%n", user.getEmail());
    System.out.printf("  Category: %s%n", category.getName());
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    return new TransactionEntity(null, amount, type, description, date, user, category);
  }
}
