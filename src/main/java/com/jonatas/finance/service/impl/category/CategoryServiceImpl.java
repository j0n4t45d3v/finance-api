package com.jonatas.finance.service.impl.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.repository.CategoryRepository;
import com.jonatas.finance.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<Category> findAllByUser(User user) {
        return this.categoryRepository.findAllByUser(user);
  }

}
