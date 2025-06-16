package br.com.managementfinanceapi.adapter.out.repository.category;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.managementfinanceapi.adapter.in.dto.category.TotalByCategoryView;
import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
  Optional<CategoryEntity> findByUserIdAndName(Long id, String name);
  boolean existsByName(String name);

  @Query(name="CategoryEntity.findTotalByCategory")
  Page<TotalByCategoryView> findTotalByCategory(
      @Param("month") Integer month,
      @Param("year") Integer year,
      @Param("userId") Long userId,
      Pageable page
  );

}