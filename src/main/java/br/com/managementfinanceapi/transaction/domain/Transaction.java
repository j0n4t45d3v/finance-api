package br.com.managementfinanceapi.transaction.domain;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.common.baseentity.TimestampEntity;
import br.com.managementfinanceapi.transaction.domain.converters.TransactionTypeConverter;
import br.com.managementfinanceapi.transaction.domain.dtos.AddTransaction;
import br.com.managementfinanceapi.transaction.domain.enums.TransactionType;
import br.com.managementfinanceapi.user.domain.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
public class Transaction extends TimestampEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "amount")
  private BigDecimal amount;
  @Convert(converter = TransactionTypeConverter.class)
  private TransactionType type;
  private String description;
  @Column(name = "date_transaction")
  private LocalDateTime date;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;

  public Transaction(
      Long id,
      BigDecimal value,
      TransactionType type,
      String description,
      LocalDateTime date,
      User user,
      Category category
  ) {
    this.id = id;
    this.amount = value;
    this.type = type;
    this.description = description;
    this.date = date;
    this.user = user;
    this.category = category;
  }

  public Transaction() {
  }

  public Transaction(AddTransaction body, User user, Category category) {
    this.amount = body.amount();
    this.type = body.type();
    this.description = body.description();
    this.date = body.date();
    this.user = user;
    this.category = category;
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public TransactionType getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public User getUser() {
    return user;
  }

  public Category getCategory() {
    return category;
  }
}