package com.jonatas.finance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.Category.Name;
import com.jonatas.finance.domain.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByIdAndUser(Long id, User user);

  Optional<Category> findByNameAndUser(Name name, User user);

  List<Category> findAllByUser(User user);
   
}
