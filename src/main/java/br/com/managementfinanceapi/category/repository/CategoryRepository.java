package br.com.managementfinanceapi.category.repository;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.movimentation.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
  boolean existsByName(String name);
}
