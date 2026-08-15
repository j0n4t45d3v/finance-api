package com.jonatas.finance.wallet;

import java.util.List;

import com.jonatas.finance.auth.User;

public interface CategoryService {

  List<Category> findAllByUser(User user);

}
