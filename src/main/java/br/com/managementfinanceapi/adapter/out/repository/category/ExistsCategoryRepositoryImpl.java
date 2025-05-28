package br.com.managementfinanceapi.adapter.out.repository.category;

import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.port.out.category.ExistsCategoryRepositoryPort;

@Component
public class ExistsCategoryRepositoryImpl implements ExistsCategoryRepositoryPort {

  private final CategoryRepository repository;

  public ExistsCategoryRepositoryImpl(CategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean byId(Long id) {
    return this.repository.existsById(id);
  }

  @Override
  public boolean byName(String name) {
    return this.repository.existsByName(name);
  }

}