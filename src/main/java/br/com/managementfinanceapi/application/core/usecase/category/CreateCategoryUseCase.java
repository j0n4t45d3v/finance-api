package br.com.managementfinanceapi.application.core.usecase.category;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.exception.CategoryAlreadyExistsException;
import br.com.managementfinanceapi.application.port.in.category.CreateCategoryPort;
import br.com.managementfinanceapi.application.port.out.category.ExistsCategoryRepositoryPort;
import br.com.managementfinanceapi.application.port.out.category.SaveCategoryRepositoryPort;

public class CreateCategoryUseCase implements CreateCategoryPort {

  private final SaveCategoryRepositoryPort saveCategoryRepositoryPort;
  private final ExistsCategoryRepositoryPort existsCategoryRepositoryPort;

  public CreateCategoryUseCase(
    SaveCategoryRepositoryPort saveCategoryRepositoryPort,
    ExistsCategoryRepositoryPort existsCategoryRepositoryPort
  ) {
    this.saveCategoryRepositoryPort = saveCategoryRepositoryPort;
    this.existsCategoryRepositoryPort = existsCategoryRepositoryPort;
  }

  @Override
  public CategoryDomain execute(CategoryDomain category) {
    boolean categoryAlreadyExists = 
      this.existsCategoryRepositoryPort.byName(category.getName());

    if(categoryAlreadyExists) throw new CategoryAlreadyExistsException();

    return this.saveCategoryRepositoryPort.execute(category);
  }
}