package br.com.managementfinanceapi.application.core.domain.category;

import java.math.BigDecimal;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public class CategoryDomain  {

  private Long id;
  private String name;
  private BigDecimal creditLimit;
  private UserDomain user;

  public CategoryDomain(
      Long id,
      String name,
      BigDecimal creditLimit,
      UserDomain user
  ) {
    this.id = id;
    this.name = name;
    this.creditLimit = creditLimit;
    this.user = user;
  }

  public CategoryDomain() {}

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public UserDomain getUser() {
    return user;
  }

}