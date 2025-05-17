package br.com.managementfinanceapi.application.core.domain.category;

import br.com.managementfinanceapi.application.core.domain.category.dto.CreateCategory;
import br.com.managementfinanceapi.adapter.in.entity.TimestampEntity;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import jakarta.persistence.*;

import java.math.BigDecimal;

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
  private UserDomain userDomain;

  public Category(Long id, String name, BigDecimal creditLimit, UserDomain userDomain) {
    this.id = id;
    this.name = name;
    this.creditLimit = creditLimit;
    this.userDomain = userDomain;
  }

  public Category(CreateCategory category) {
    this.name = category.name();
    this.creditLimit = category.creditLimit();
    UserDomain userDomain = new UserDomain();
    userDomain.setId(category.userId());
    this.userDomain = userDomain;
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

  public UserDomain getUser() {
    return userDomain;
  }
}