package br.com.managementfinanceapi.adapter.out.entity.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, Character> {
  @Override
  public Character convertToDatabaseColumn(TransactionType attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getValue();
  }

  @Override
  public TransactionType convertToEntityAttribute(Character dbData) {
    if(dbData == null) {
      return null;
    }
    return TransactionType.valueOf(dbData);
  }
}