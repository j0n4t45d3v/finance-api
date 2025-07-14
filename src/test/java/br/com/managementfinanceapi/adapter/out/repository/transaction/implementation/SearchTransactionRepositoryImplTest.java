package br.com.managementfinanceapi.adapter.out.repository.transaction.implementation;

import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.adapter.out.mapper.category.CategoryMapper;
import br.com.managementfinanceapi.adapter.out.mapper.transaction.TransactionMapper;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageDto;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
import br.com.managementfinanceapi.factory.TransactionFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@Import({
    SearchTransactionRepositoryImpl.class,
    CategoryMapper.class,
    TransactionMapper.class,
    UserMapper.class
})
class SearchTransactionRepositoryImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private SearchTransactionRepositoryImpl searchTransactionRepository;
  private TransactionEntity transaction;
  @BeforeEach
  void setUp() {
    this.transaction = TransactionFactory.createFakeData();
    TransactionEntity transactionSecond = TransactionFactory.createFakeData();
    this.entityManager.persist(transactionSecond.getUser());
    this.entityManager.persist(transactionSecond.getCategory());
    this.entityManager.persist(transactionSecond);

    this.entityManager.persist(this.transaction.getUser());
    this.entityManager.persist(this.transaction.getCategory());
    this.entityManager.persist(this.transaction);
    this.entityManager.flush();
  }

  @Test
  @DisplayName("should find all transaction with filters")
  void shouldFindAllUserTransactionWithFilters() {
    DateRange range = new DateRange(transaction.getDate().toLocalDate(), transaction.getDate().toLocalDate());
    SearchAllFilters filters = new SearchAllFilters(this.transaction.getUser().getId(), range, null, null);
    PageDto<TransactionDomain> content = this.searchTransactionRepository.all(filters);
    assertEquals(1, content.total());
  }

  @Test
  @DisplayName("should find all user transaction paginate")
  void shouldFindAllUserTransactionPaginate() {
    PageDto<TransactionDomain> content = this.searchTransactionRepository.allByUser(this.transaction.getUser().getId());
    assertEquals(1, content.total());
  }

  @Test
  @DisplayName("should find all user transaction between date range")
  void shouldFindAllUserTransactionBetweenDateRange() {
    DateRange range = new DateRange(transaction.getDate().toLocalDate(), transaction.getDate().toLocalDate());
    List<TransactionDomain> content = this.searchTransactionRepository.allByUser(this.transaction.getUser().getId(), range);
    assertEquals(1, content.size());
  }

  @Test
  @DisplayName("should find transaction with id informed")
  void shouldFindTransactionWithIdInformed() {
    Optional<TransactionDomain> transaction = this.searchTransactionRepository.byId(this.transaction.getId());
    assertTrue(transaction.isPresent());
    assertEquals(this.transaction.getId(), transaction.get().getId());
    assertEquals(this.transaction.getDescription(), transaction.get().getDescription());
    assertEquals(this.transaction.getAmount(), transaction.get().getAmount());
  }
}