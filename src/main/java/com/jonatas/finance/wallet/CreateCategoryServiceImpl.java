package com.jonatas.finance.wallet;

import org.springframework.stereotype.Service;

import com.jonatas.finance.common.CreateService;
import com.jonatas.finance.common.exception.DomainException;

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
