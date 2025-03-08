package br.com.managementfinanceapi.category.usecase;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.category.gateway.FindAllCategoriesGateway;
import br.com.managementfinanceapi.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllCategoriesUseCase implements FindAllCategoriesGateway {
  private final CategoryRepository repository;

  public FindAllCategoriesUseCase(CategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Category> execute() {
    return this.repository.findAll();
  }
}
