package br.com.managementfinanceapi.application.port.in.category;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;

public interface CreateCategoryPort {
  CategoryDomain execute(CategoryDomain category);
}