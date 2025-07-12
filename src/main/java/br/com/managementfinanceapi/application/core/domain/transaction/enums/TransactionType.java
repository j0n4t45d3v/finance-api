package br.com.managementfinanceapi.application.core.domain.transaction.enums;

public enum TransactionType {
  INCOME('I'),
  EXPENSE('E');

  private final Character value;

  TransactionType(Character value) {
    this.value = value;
  }

  public Character getValue() {
    return value;
  }

  public String getDescription() {
    return this.equals(TransactionType.EXPENSE) ? "Despesa" : "Receita";
  }

  public static TransactionType valueOf(Character value) {
    for (TransactionType type : TransactionType.values()) {
      if (type.getValue().equals(value)) {
        return type;
      }
    }
    return null;
  }
}