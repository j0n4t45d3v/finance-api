package com.jonatas.finance.wallet;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.Result;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Result<Category> create(Category category) {
        var categoryFound = this.categoryRepository.findByNameAndUser(category.getName(), category.getUser());
        if (categoryFound.isPresent()) {
            return Result.failure(CategoryErrorCode.ALREADY_EXISTS_CATEGORY_WITH_NAME);
        }
        var categorySaved = this.categoryRepository.save(category);
        return Result.success(categorySaved);
    }

    public List<Category> findAllByUser(User user) {
        return this.categoryRepository.findAllByUser(user);
    }
}
