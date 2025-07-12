package br.com.managementfinanceapi.factory;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CategoryFactory {

  @FunctionalInterface
  interface FakeDataCategory {
    CategoryEntity make(UserEntity user);
  }

  private static final int MAX_DATA_SIZE = 4;
  private static final List<FakeDataCategory> FAKE_DATA = new ArrayList<>(MAX_DATA_SIZE);
  static {
    FAKE_DATA.add((user) -> CategoryFactory.create("Salary", BigDecimal.valueOf(9999.99), user));
    FAKE_DATA.add((user) -> CategoryFactory.create("Food", BigDecimal.valueOf(1000.00), user));
    FAKE_DATA.add((user) -> CategoryFactory.create("Game pass", BigDecimal.valueOf(25.50), user));
    FAKE_DATA.add((user) -> CategoryFactory.create("Leisure", BigDecimal.valueOf(300.00), user));
  }

  public static CategoryEntity createFakeData(){
    return CategoryFactory.createFakeData(UserFactory.createFakeData());
  }

  public static CategoryEntity createFakeData(UserEntity user){
    FakeDataCategory fakeDataCategory = FAKE_DATA.get(ThreadLocalRandom.current().nextInt(MAX_DATA_SIZE));
    return fakeDataCategory.make(user);
  }

  public static CategoryEntity create(String name, BigDecimal creditLimit, UserEntity user){
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    System.out.println("Create Category: ");
    System.out.printf("  Name: %s%n", name);
    System.out.printf("  Credit limit: R$ %.2f%n", creditLimit.doubleValue());
    System.out.printf("  User: %s%n", user.getEmail());
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    return new CategoryEntity(null, name, creditLimit, user);
  }
}
