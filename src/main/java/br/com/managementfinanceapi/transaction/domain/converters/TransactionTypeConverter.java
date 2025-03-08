package br.com.managementfinanceapi.transaction.domain.converters;

import br.com.managementfinanceapi.transaction.domain.enums.TransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, Character> {
  private static final Logger log = LoggerFactory.getLogger(TransactionTypeConverter.class);
  @Override
  public Character convertToDatabaseColumn(TransactionType attribute) {
    log.info("Converting TransactionType to Character: {}", attribute);
    if (attribute == null) {
      return null;
    }
    log.info("Value Converted: {}", attribute.getValue());
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
