package br.com.managementfinanceapi.factory;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;

import java.util.UUID;

public class UserFactory {

  public static UserEntity createFakeData(){
    return UserFactory.create("john"+ UUID.randomUUID() + "@doe.test", "secretPassword");
  }

  public static UserEntity create(String email, String password){
    return new UserEntity(email, password);
  }

}
