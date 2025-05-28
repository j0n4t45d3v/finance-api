package br.com.managementfinanceapi.adapter.out.entity.category;

import java.math.BigDecimal;

import br.com.managementfinanceapi.adapter.out.entity.TimestampEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryEntity extends TimestampEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(name = "credit_limit")
  private BigDecimal creditLimit;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  public CategoryEntity(Long id, String name, BigDecimal creditLimit, UserEntity user) {
    this.id = id;
    this.name = name;
    this.creditLimit = creditLimit;
    this.user = user;
  }

  public CategoryEntity() {
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public BigDecimal getCreditLimit() {
    return this.creditLimit;
  }

  public UserEntity getUser() {
    return this.user;
  }

  
}