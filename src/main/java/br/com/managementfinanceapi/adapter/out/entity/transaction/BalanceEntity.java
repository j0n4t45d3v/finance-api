package br.com.managementfinanceapi.adapter.out.entity.transaction;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "balances")
public class BalanceEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal amount;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;
  private Short month;
  private Short year;

  public BalanceEntity() {
  }

  public BalanceEntity(Long id, BigDecimal amount, UserEntity user, Short month, Short year) {
    this.id = id;
    this.amount = amount;
    this.user = user;
    this.month = month;
    this.year = year;
  }

  public BalanceEntity(BigDecimal amount, UserEntity user, Short month, Short year) {
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

  public UserEntity getUser() {
    return this.user;
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