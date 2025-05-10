package br.com.managementfinanceapi.application.core.usecase.category;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.port.in.category.FindAllCategoriesGateway;
import br.com.managementfinanceapi.adapter.out.repository.category.CategoryRepository;
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
