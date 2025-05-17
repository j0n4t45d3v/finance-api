package br.com.managementfinanceapi.application.core.domain.transaction;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.adapter.in.entity.TimestampEntity;
import br.com.managementfinanceapi.application.core.domain.transaction.converters.TransactionTypeConverter;
import br.com.managementfinanceapi.application.core.domain.transaction.dtos.AddTransaction;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
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
  private UserDomain userDomain;
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;

  public Transaction(
      Long id,
      BigDecimal value,
      TransactionType type,
      String description,
      LocalDateTime date,
      UserDomain userDomain,
      Category category
  ) {
    this.id = id;
    this.amount = value;
    this.type = type;
    this.description = description;
    this.date = date;
    this.userDomain = userDomain;
    this.category = category;
  }

  public Transaction() {
  }

  public Transaction(AddTransaction body, UserDomain userDomain, Category category) {
    this.amount = body.amount();
    this.type = body.type();
    this.description = body.description();
    this.date = body.date();
    this.userDomain = userDomain;
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

  public UserDomain getUser() {
    return userDomain;
  }

  public Category getCategory() {
    return category;
  }
}