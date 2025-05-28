package br.com.managementfinanceapi.application.port.out.category;

import java.util.List;
import java.util.Optional;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;

public interface SearchCategoryRepositoryPort {

  List<CategoryDomain> all();
  Optional<CategoryDomain> byUserIdAndName(Long userId, String name);
  
}