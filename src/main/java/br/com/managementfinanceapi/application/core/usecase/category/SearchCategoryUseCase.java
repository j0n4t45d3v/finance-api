package br.com.managementfinanceapi.application.core.usecase.category;

import java.util.List;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.exception.CategoryDoesNotExistsException;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.out.category.SearchCategoryRepositoryPort;

public class SearchCategoryUseCase implements SearchCategoryPort {

  private final SearchCategoryRepositoryPort searchCategoryRepositoryPort;

  public SearchCategoryUseCase(SearchCategoryRepositoryPort searchCategoryRepositoryPort) {
    this.searchCategoryRepositoryPort = searchCategoryRepositoryPort;
  }

  @Override
  public List<CategoryDomain> all() {
    return this.searchCategoryRepositoryPort.all();
  }

  @Override
  public CategoryDomain byName(Long userId, String name) {
    return this.searchCategoryRepositoryPort.byUserIdAndName(userId, name)
        .orElseThrow(() -> new CategoryDoesNotExistsException());
  }
}