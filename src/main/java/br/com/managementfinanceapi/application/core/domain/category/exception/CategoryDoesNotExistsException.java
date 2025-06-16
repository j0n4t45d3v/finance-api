package br.com.managementfinanceapi.application.core.domain.category.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.NotFoundException;

public class CategoryDoesNotExistsException extends NotFoundException {

  public CategoryDoesNotExistsException() {
    super("Categoria não existe!");
  }
}