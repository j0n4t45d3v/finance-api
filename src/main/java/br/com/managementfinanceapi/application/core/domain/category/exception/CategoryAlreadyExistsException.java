package br.com.managementfinanceapi.application.core.domain.category.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.ConflictException;

public class CategoryAlreadyExistsException extends ConflictException {
  public CategoryAlreadyExistsException(String message) {
    super(message);

  }
  public CategoryAlreadyExistsException() {
    super("Categoria já existe!");
  }
}