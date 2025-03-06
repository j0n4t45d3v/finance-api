package br.com.managementfinanceapi.category.gateway;

import br.com.managementfinanceapi.category.domain.Category;

import java.util.List;

public interface FindAllCategoriesGateway {
  List<Category> execute();
}
