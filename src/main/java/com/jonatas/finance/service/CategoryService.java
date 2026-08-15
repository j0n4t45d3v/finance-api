package com.jonatas.finance.service;

import java.util.List;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.domain.Category;

public interface CategoryService {

  List<Category> findAllByUser(User user);

}
