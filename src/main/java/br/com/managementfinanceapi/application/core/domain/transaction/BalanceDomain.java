package br.com.managementfinanceapi.application.core.domain.transaction;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceDomain {
  private Long id;
  private BigDecimal amount;
  private UserDomain userDomain;
  private Short month;
  private Short year;

  public BalanceDomain() {
    this.monthYearNow();
  }

  public BalanceDomain(Long id, BigDecimal amount, UserDomain userDomain, Short month, Short year) {
    this.id = id;
    this.amount = amount;
    this.userDomain = userDomain;
    this.month = month;
    this.year = year;
  }

  public BalanceDomain(BigDecimal amount, UserDomain userDomain, Short month, Short year) {
    this.amount = amount;
    this.userDomain = userDomain;
    this.month = month;
    this.year = year;
  }

  public BalanceDomain(BigDecimal amount, UserDomain userDomain) {
    this.amount = amount;
    this.userDomain = userDomain;
    this.monthYearNow();
  }

  private void monthYearNow() {
    LocalDate dateNow = LocalDate.now();
    this.month = (short) dateNow.getMonthValue();
    this.year = (short) dateNow.getYear();
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public UserDomain getUser() {
    return userDomain;
  }

  public Short getMonth() {
    return month;
  }

  public Short getYear() {
    return year;
  }

  public void addAmount(BigDecimal amount) {
    this.amount = this.amount.add(amount);
  }

  public void signUser(UserDomain user) {
    this.userDomain = user;
  }

}