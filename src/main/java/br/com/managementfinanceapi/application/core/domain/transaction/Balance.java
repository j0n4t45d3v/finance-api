package br.com.managementfinanceapi.application.core.domain.transaction;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "balances")
public class Balance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal amount;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserDomain userDomain;
  private Short month;
  private Short year;

  public Balance() {
  }

  public Balance(Long id, BigDecimal amount, UserDomain userDomain, Short month, Short year) {
    this.id = id;
    this.amount = amount;
    this.userDomain = userDomain;
    this.month = month;
    this.year = year;
  }

  public Balance(BigDecimal amount, UserDomain userDomain, Short month, Short year) {
    this.amount = amount;
    this.userDomain = userDomain;
    this.month = month;
    this.year = year;
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
}
