package br.com.managementfinanceapi.adapter.out.repository.category.implementation;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.category.CategoryMapper;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Import({
    SearchCategoryRepositoryImpl.class,
    CategoryMapper.class,
    UserMapper.class
})
class SearchCategoryRepositoryImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private SearchCategoryRepositoryImpl searchCategoryRepository;
  private CategoryEntity category;

  @BeforeEach
  void setUp() {
    UserEntity user = new UserEntity("john@doe.test", "secretpassword");
    this.category = new CategoryEntity(null, "Test category", BigDecimal.TEN, user);
    TransactionEntity incomeTransaction = new TransactionEntity(
        null,
        BigDecimal.TEN,
        TransactionType.INCOME,
        "Test transaction",
        LocalDateTime.now(),
        user,
        this.category
    );
    TransactionEntity expenseTransaction = new TransactionEntity(
        null,
        BigDecimal.TEN,
        TransactionType.EXPENSE,
        "Test transaction",
        LocalDateTime.now(),
        user,
        this.category
    );
    this.entityManager.persist(user);
    this.entityManager.persist(this.category);
    this.entityManager.persist(incomeTransaction);
    this.entityManager.persist(expenseTransaction);
    this.entityManager.flush();
  }

  @Test
  @DisplayName("should find all categories")
  void shouldFindAllCategories() {
    List<CategoryDomain> categories = this.searchCategoryRepository.all();
    assertEquals(1, categories.size());
  }

  @Test
  @DisplayName("should find by name user category")
  void shouldFindByNameUserCategory() {
    Optional<CategoryDomain> category =
        this.searchCategoryRepository.byUserIdAndName(this.category.getUser().getId(), this.category.getName());

    assertTrue(category.isPresent());
    assertEquals(this.category.getId(), category.get().getId());
    assertEquals(this.category.getName(), category.get().getName());
    assertEquals(this.category.getCreditLimit(), category.get().getCreditLimit());
  }

  @Test
  @DisplayName("should find by id user category")
  void shouldFindByIdUserCategory() {
      Optional<CategoryDomain> category =
          this.searchCategoryRepository.byUserIdAndId(this.category.getUser().getId(), this.category.getId());

      assertTrue(category.isPresent());
      assertEquals(this.category.getId(), category.get().getId());
      assertEquals(this.category.getName(), category.get().getName());
      assertEquals(this.category.getCreditLimit(), category.get().getCreditLimit());
  }

  @Test
  @DisplayName("should return summary with incomes and expenses by category")
  void shouldReturnSummaryWithIncomesAndExpensesByCategory() {
    List<CategoryTransactionSummary> summary = this.searchCategoryRepository.getSummaryIncomeAndExpencesTotals(
        this.category.getUser().getId(),
        LocalDate.now().atStartOfDay(),
        LocalDateTime.now()
    );
    assertEquals(1, summary.size());
    CategoryTransactionSummary actual = summary.get(0);
    assertEquals(this.category.getName(), actual.name());
    assertEqualsBigDecimal(this.category.getCreditLimit(), actual.creditLimit());
    assertEqualsBigDecimal(BigDecimal.TEN, actual.income());
    assertEqualsBigDecimal(BigDecimal.TEN, actual.expence());
    assertEqualsBigDecimal(BigDecimal.TEN, actual.totalIncome());
    assertEqualsBigDecimal(BigDecimal.TEN, actual.totalExpence());
    assertEqualsBigDecimal(BigDecimal.ONE, actual.incomePercentage());
    assertEqualsBigDecimal(BigDecimal.ONE, actual.expencePercentage());
  }

  private static void assertEqualsBigDecimal(BigDecimal expected, BigDecimal actual) {
    assertEqualsBigDecimal(expected, actual, 2);
  }
  private static void assertEqualsBigDecimal(BigDecimal expected, BigDecimal actual, int scaleCompare) {
    assertEquals(expected.setScale(2, RoundingMode.HALF_EVEN), actual.setScale(scaleCompare, RoundingMode.HALF_EVEN));
  }
}