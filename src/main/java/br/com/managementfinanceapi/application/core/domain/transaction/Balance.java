package br.com.managementfinanceapi.application.core.domain.transaction;

import br.com.managementfinanceapi.application.core.domain.user.User;
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
  private User user;
  private Short month;
  private Short year;

  public Balance() {
  }

  public Balance(Long id, BigDecimal amount, User user, Short month, Short year) {
    this.id = id;
    this.amount = amount;
    this.user = user;
    this.month = month;
    this.year = year;
  }

  public Balance(BigDecimal amount, User user, Short month, Short year) {
    this.amount = amount;
    this.user = user;
    this.month = month;
    this.year = year;
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public User getUser() {
    return user;
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
