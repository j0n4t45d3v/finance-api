package com.jonatas.finance.wallet;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jonatas.finance.auth.User;

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
