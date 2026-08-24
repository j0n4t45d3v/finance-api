package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.wallet.Category.Name;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByIdAndUser(Long id, User user);

  Optional<Category> findByNameAndUser(Name name, User user);

  List<Category> findAllByUser(User user);
}
