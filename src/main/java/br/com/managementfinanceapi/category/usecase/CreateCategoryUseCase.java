package br.com.managementfinanceapi.category.usecase;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.category.domain.dto.CreateCategory;
import br.com.managementfinanceapi.category.exception.CategoryAlreadyExistsException;
import br.com.managementfinanceapi.category.gateway.CreateCategoryGateway;
import br.com.managementfinanceapi.category.repository.CategoryRepository;
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
