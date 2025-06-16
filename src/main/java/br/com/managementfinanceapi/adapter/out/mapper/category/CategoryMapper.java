package br.com.managementfinanceapi.adapter.out.mapper.category;

import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

@Component
public class CategoryMapper implements Mapper<CategoryEntity, CategoryDomain> {

  private final Mapper<UserEntity, UserDomain> userMapper;

  public CategoryMapper(Mapper<UserEntity, UserDomain> userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public CategoryEntity toEntity(CategoryDomain domain) {
    return new CategoryEntity(
        domain.getId(),
        domain.getName(),
        domain.getCreditLimit(),
        this.userMapper.toEntity(domain.getUser())
    );
  }

  @Override
  public CategoryDomain toDomain(CategoryEntity entity) {
    return new CategoryDomain(
        entity.getId(),
        entity.getName(),
        entity.getCreditLimit(),
        this.userMapper.toDomain(entity.getUser())
    );
  }

}