package br.com.managementfinanceapi.adapter.out.repository.category;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.core.domain.category.dto.TotalByCategoryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
  boolean existsByName(String name);

  @Query(name="Category.findTotalByCategory")
  Page<TotalByCategoryView> findTotalByCategory(
      @Param("month") Integer month,
      @Param("year") Integer year,
      @Param("userId") Long userId,
      Pageable page
  );

}
