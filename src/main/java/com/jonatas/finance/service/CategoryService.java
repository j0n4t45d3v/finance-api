package com.jonatas.finance.service;

import java.util.List;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.User;

public interface CategoryService {

  List<Category> findAllByUser(User user);

}
