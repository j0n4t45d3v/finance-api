package br.com.managementfinanceapi.adapter.out.repository.transaction.implementation;

import br.com.managementfinanceapi.adapter.out.entity.transaction.BalanceEntity;
import br.com.managementfinanceapi.adapter.out.mapper.transaction.BalanceMapper;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
import br.com.managementfinanceapi.factory.BalanceFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Import({
    SearchBalanceRepositoryImpl.class,
    BalanceMapper.class,
    UserMapper.class
})
class SearchBalanceRepositoryImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private SearchBalanceRepositoryImpl searchBalanceRepository;
  private BalanceEntity balance;

  @BeforeEach
  void setUp() {
    List<BalanceEntity> balances = new ArrayList<>(BalanceFactory.createFakeListData(3));
    balances.sort(Comparator.comparing(BalanceEntity::getMonth));
    this.balance = balances.get(0);

    this.entityManager.persist(this.balance.getUser());
    for (BalanceEntity balanceEntity : balances) {
      this.entityManager.persist(balanceEntity);
    }
    this.entityManager.flush();
  }

  @Test
  @DisplayName("should find all user balances")
  void shouldFindAllUserBalances() {
    List<BalanceDomain> balances = this.searchBalanceRepository.allByUser(this.balance.getUser().getId());
    assertEquals(3, balances.size());
    for (BalanceDomain balance : balances) {
      assertEquals(this.balance.getUser().getId(), balance.getUser().getId());
    }
  }

  @Test
  @DisplayName("should find the user monthly balance")
  void shouldFindTheUserMonthlyBalance() {
    Optional<BalanceDomain> balance = this.searchBalanceRepository
        .userBalanceForMonthYear(this.balance.getUser().getId(), this.balance.getMonth(), this.balance.getYear());
    assertTrue(balance.isPresent());
    assertEquals(this.balance.getUser().getId(), balance.get().getUser().getId());
  }

  @Test
  @DisplayName("should find all user balances after the month ")
  void userBalancesAfterMonthYear() {
    List<BalanceDomain> balances = this.searchBalanceRepository
        .userBalancesAfterMonthYear(this.balance.getMonth(), this.balance.getYear(), this.balance.getUser().getId());

    assertEquals(2, balances.size());
    for (BalanceDomain balance : balances) {
      assertNotEquals(this.balance.getId(), balance.getId());
    }
  }
}