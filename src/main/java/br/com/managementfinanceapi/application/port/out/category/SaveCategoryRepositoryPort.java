package br.com.managementfinanceapi.application.port.out.category;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;

public interface SaveCategoryRepositoryPort {
  CategoryDomain execute(CategoryDomain category);
}