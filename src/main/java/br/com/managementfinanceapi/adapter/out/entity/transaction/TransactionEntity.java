package br.com.managementfinanceapi.adapter.out.entity.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.managementfinanceapi.adapter.out.entity.TimestampEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class TransactionEntity extends TimestampEntity{
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
  private UserEntity user;
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;

  public TransactionEntity(
      Long id,
      BigDecimal value,
      TransactionType type,
      String description,
      LocalDateTime date,
      UserEntity user,
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

  public TransactionEntity() {
  }

  public Long getId() {
    return this.id;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public TransactionType getType() {
    return this.type;
  }

  public String getDescription() {
    return this.description;
  }

  public LocalDateTime getDate() {
    return this.date;
  }

  public UserEntity getUser() {
    return this.user;
  }

  public Category getCategory() {
    return this.category;
  }
}