package br.com.managementfinanceapi.movimentation.domain.enums;

public enum TransactionType {
  INCOME('I'),
  EXPENSE('E');

  private Character value;

  TransactionType(Character value) {
    this.value = value;
  }

  public Character getValue() {
    return value;
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
