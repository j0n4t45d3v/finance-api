package br.com.managementfinanceapi.adapter.out.repository.transaction.implementation;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.category.CategoryMapper;
import br.com.managementfinanceapi.adapter.out.mapper.transaction.TransactionMapper;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
import br.com.managementfinanceapi.factory.CategoryFactory;
import br.com.managementfinanceapi.factory.UserFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@Import({
    SaveTransactionRepositoryImpl.class,
    TransactionMapper.class,
    UserMapper.class,
    CategoryMapper.class
})
class SaveTransactionRepositoryImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private SaveTransactionRepositoryImpl saveTransactionRepository;

  private UserDomain user;
  private CategoryDomain category;

  @BeforeEach
  void setUp() {
    UserEntity user = UserFactory.createFakeData();
    CategoryEntity category = CategoryFactory.createFakeData(user);
    this.entityManager.persist(user);
    this.entityManager.persist(category);
    this.entityManager.flush();
    this.user = this.userMapper.toDomain(user);
    this.category = this.categoryMapper.toDomain(category);
  }

  @Test
  @DisplayName("should insert a transaction")
  void execute() {
    TransactionDomain transaction = new TransactionDomain(
        null,
        BigDecimal.TEN,
        TransactionType.INCOME,
        "Test insert",
        LocalDateTime.now(),
        this.user,
        this.category
    );
    TransactionDomain transactionSaved = this.saveTransactionRepository.execute(transaction);

    assertNotNull(transactionSaved.getId());
    assertEquals(transaction.getDescription(), transactionSaved.getDescription());
    assertEquals(transaction.getType(), transactionSaved.getType());
    assertEquals(transaction.getDate(), transactionSaved.getDate());
    assertEquals(transaction.getUser().getId(), transactionSaved.getUser().getId());
    assertEquals(transaction.getCategoryId(), transactionSaved.getCategoryId());
  }
}