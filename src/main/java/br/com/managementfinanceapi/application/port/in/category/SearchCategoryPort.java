package br.com.managementfinanceapi.application.port.in.category;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;

import java.util.List;

public interface SearchCategoryPort {
  List<CategoryDomain> all();
  CategoryDomain byName(Long userId, String name);
}