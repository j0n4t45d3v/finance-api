package br.com.managementfinanceapi.category.gateway;

import br.com.managementfinanceapi.category.domain.dto.FiltersTotalByCategory;
import br.com.managementfinanceapi.category.domain.dto.TotalByCategoryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchTotalByCategoryGateway {
  Page<TotalByCategoryView> execute(FiltersTotalByCategory filters, Pageable page);
}
