package br.com.managementfinanceapi.adapter.out.repository.category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.port.out.category.SearchCategoryRepositoryPort;
import jakarta.annotation.Nonnull;

@Component
public class SearchCategoryRepositoryImpl implements SearchCategoryRepositoryPort{

  private final CategoryRepository repository;
  private final Mapper<CategoryEntity, CategoryDomain> mapper;

  public SearchCategoryRepositoryImpl(
    CategoryRepository repository,
    Mapper<CategoryEntity, CategoryDomain> mapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<CategoryDomain> all() {
    return this.repository.findAll()
                          .stream()
                          .map(this.mapper::toDomain)
                          .toList();
  }

  @Override
  public Optional<CategoryDomain> byUserIdAndName(Long userId, String name) {
    return this.repository.findByUserIdAndName(userId, name)
                          .map(this.mapper::toDomain);
  }

  @Override
  public List<CategoryTransactionSummary> getSummaryIncomeAndExpencesTotals(
    @Nonnull Long userId, 
    @Nonnull LocalDateTime startDate,
    @Nonnull LocalDateTime endDate
  ) {
    return this.repository.findIncomesAndExpensesTotals(startDate, endDate, userId);
  }

  
}