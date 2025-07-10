package br.com.managementfinanceapi.factory;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UserFactory {

  private static final int MAX_DATA_SIZE = 4;
  private static final List<UserEntity> FAKE_DATA = new ArrayList<>(MAX_DATA_SIZE);
  static {
    FAKE_DATA.add(UserFactory.create("john@doe.com", "secretPassword"));
    FAKE_DATA.add(UserFactory.create("carl@test.com", "secretPassword"));
    FAKE_DATA.add(UserFactory.create("foo@bar.net", "secretPassword"));
    FAKE_DATA.add(UserFactory.create("bar@foo.wa", "secretPassword"));
  }

  public static UserEntity createFakeData(){
    return FAKE_DATA.get(ThreadLocalRandom.current().nextInt(MAX_DATA_SIZE));
  }

  public static UserEntity create(String email, String password){
    return new UserEntity(email, password);
  }

}
