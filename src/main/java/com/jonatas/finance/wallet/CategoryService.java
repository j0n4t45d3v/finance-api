package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import java.util.List;

public interface CategoryService {

  List<Category> findAllByUser(User user);
}
