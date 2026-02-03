package com.jonatas.finance.service.impl.category;

import org.springframework.stereotype.Service;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.exception.DomainException;
import com.jonatas.finance.repository.CategoryRepository;
import com.jonatas.finance.service.CreateService;

@Service
public class CreateCategoryServiceImpl implements CreateService<Category>{

  private final CategoryRepository categoryRepository;

  public CreateCategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category execute(Category category) {
    this.categoryRepository.findByNameAndUser(category.getName(), category.getUser())
        .ifPresent((_c) -> {throw new DomainException("Category already exists");});
    return this.categoryRepository.save(category);
  }

}
