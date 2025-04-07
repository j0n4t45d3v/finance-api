package br.com.managementfinanceapi.category.usecase;

import br.com.managementfinanceapi.category.domain.dto.FiltersTotalByCategory;
import br.com.managementfinanceapi.category.domain.dto.TotalByCategoryView;
import br.com.managementfinanceapi.category.gateway.SearchTotalByCategoryGateway;
import br.com.managementfinanceapi.category.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchTotalByCategoryUseCase implements SearchTotalByCategoryGateway {

  private final CategoryRepository repository;

  public SearchTotalByCategoryUseCase(CategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public Page<TotalByCategoryView> execute(FiltersTotalByCategory filters, Pageable page) {
    return this.repository.findTotalByCategory(filters.month(), filters.year(), filters.userId(), page);
  }
}
