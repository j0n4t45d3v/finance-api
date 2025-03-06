package br.com.managementfinanceapi.category.gateway;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.category.domain.dto.CreateCategory;

public interface CreateCategoryGateway {
  Category execute(CreateCategory category);
}
