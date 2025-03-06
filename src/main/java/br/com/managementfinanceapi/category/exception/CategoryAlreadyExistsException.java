package br.com.managementfinanceapi.category.exception;

import br.com.managementfinanceapi.infra.error.exceptions.ConflictException;

public class CategoryAlreadyExistsException extends ConflictException {
  public CategoryAlreadyExistsException(String message) {
    super(message);

  }
  public CategoryAlreadyExistsException() {
    super("Category already exists");
  }
}
