package br.com.managementfinanceapi.adapter.out.repository.category.implementation;

import br.com.managementfinanceapi.adapter.out.repository.category.CategoryRepository;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.port.out.category.SaveCategoryRepositoryPort;

@Component
public class SaveCategoryRepositoryImpl implements SaveCategoryRepositoryPort {

  private final CategoryRepository repository;
  private final Mapper<CategoryEntity, CategoryDomain> mapper;

  public SaveCategoryRepositoryImpl(
    CategoryRepository repository,
    Mapper<CategoryEntity, CategoryDomain> mapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public CategoryDomain execute(CategoryDomain category) {
    CategoryEntity categorySave = this.repository.save(this.mapper.toEntity(category));
    return this.mapper.toDomain(categorySave);
  }

  
}