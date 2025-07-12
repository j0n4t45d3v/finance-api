package br.com.managementfinanceapi.factory;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;

import java.util.UUID;

public class UserFactory {

  public static UserEntity createFakeData(){
    return UserFactory.create("john"+ UUID.randomUUID() + "@doe.test", "secretPassword");
  }

  public static UserEntity create(String email, String password){
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    System.out.println("Create User: ");
    System.out.printf("  Email: %s%n", email);
    System.out.printf("  Password: %s%n", password);
    System.out.println();
    System.out.println("======================================== ");
    System.out.println();
    return new UserEntity(email, password);
  }

}
