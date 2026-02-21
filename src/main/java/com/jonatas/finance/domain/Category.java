package com.jonatas.finance.domain;

import com.jonatas.finance.domain.exception.DomainException;

import jakarta.annotation.Nonnull;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_categories")
public class Category {

  public record Name(String value) {
    public Name{
      if (value == null || value.isBlank())  {
        throw new DomainException("Category name cannot be empty");
      }
      if (value.length() > 50) {
        throw new DomainException("Category name cannot be greater to 50");
      }
    }
  }

  public enum Type {
    EXPENSE, INCOME;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "name"))
  private Name name;

  @Enumerated(EnumType.STRING)
  private Type type;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  protected Category() {
  }

  public Category(User user) {
    this.user = user;
  }

  public Category(@Nonnull Name name, @Nonnull Type type, @Nonnull User user) {
    this.name = name;
    this.type = type;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public Name getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  public User getUser() {
    return this.user;
  }

  public String getNameValue() {
        return this.name.value();
  }

}
