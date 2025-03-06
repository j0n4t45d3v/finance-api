package br.com.managementfinanceapi.category.domain;

import br.com.managementfinanceapi.category.domain.dto.CreateCategory;
import br.com.managementfinanceapi.common.baseentity.TimestampEntity;
import br.com.managementfinanceapi.movimentation.domain.Transaction;
import br.com.managementfinanceapi.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends TimestampEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(name = "credit_limit")
  private BigDecimal creditLimit;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  public Category(Long id, String name, BigDecimal creditLimit, User user) {
    this.id = id;
    this.name = name;
    this.creditLimit = creditLimit;
    this.user = user;
  }

  public Category(CreateCategory category) {
    this.name = category.name();
    this.creditLimit = category.creditLimit();
    User user = new User();
    user.setId(category.userId());
    this.user = user;
  }

  public Category() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public User getUser() {
    return user;
  }
}