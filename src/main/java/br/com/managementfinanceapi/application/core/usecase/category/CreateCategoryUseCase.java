package br.com.managementfinanceapi.application.core.usecase.category;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.core.domain.category.dto.CreateCategory;
import br.com.managementfinanceapi.infra.error.exceptions.category.CategoryAlreadyExistsException;
import br.com.managementfinanceapi.application.port.in.category.CreateCategoryGateway;
import br.com.managementfinanceapi.adapter.out.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUseCase implements CreateCategoryGateway {

  private final CategoryRepository repository;

  public CreateCategoryUseCase(CategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public Category execute(CreateCategory category) {
    boolean categoryAlreadyExists = this.repository.existsByName(category.name());
    if(categoryAlreadyExists) throw new CategoryAlreadyExistsException();
    Category categoryEntity = new Category(category);
    return this.repository.save(categoryEntity);
  }
}
