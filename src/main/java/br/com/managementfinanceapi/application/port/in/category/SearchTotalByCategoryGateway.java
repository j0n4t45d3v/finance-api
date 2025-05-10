package br.com.managementfinanceapi.application.port.in.category;

import br.com.managementfinanceapi.application.core.domain.category.dto.FiltersTotalByCategory;
import br.com.managementfinanceapi.application.core.domain.category.dto.TotalByCategoryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchTotalByCategoryGateway {
  Page<TotalByCategoryView> execute(FiltersTotalByCategory filters, Pageable page);
}
