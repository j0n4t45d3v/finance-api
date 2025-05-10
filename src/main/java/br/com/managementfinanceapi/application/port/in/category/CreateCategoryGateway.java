package br.com.managementfinanceapi.application.port.in.category;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.core.domain.category.dto.CreateCategory;

public interface CreateCategoryGateway {
  Category execute(CreateCategory category);
}
