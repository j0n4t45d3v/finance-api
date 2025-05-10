package br.com.managementfinanceapi.application.core.usecase.category;

import br.com.managementfinanceapi.application.core.domain.category.dto.FiltersTotalByCategory;
import br.com.managementfinanceapi.application.core.domain.category.dto.TotalByCategoryView;
import br.com.managementfinanceapi.application.port.in.category.SearchTotalByCategoryGateway;
import br.com.managementfinanceapi.adapter.out.repository.category.CategoryRepository;
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
