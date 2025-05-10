package br.com.managementfinanceapi.application.port.in.category;

import br.com.managementfinanceapi.application.core.domain.category.Category;

import java.util.List;

public interface FindAllCategoriesGateway {
  List<Category> execute();
}
