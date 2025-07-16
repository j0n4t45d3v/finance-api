package br.com.managementfinanceapi.adapter.out.repository.transaction.implementation;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.transaction.BalanceMapper;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@Import({
    SaveBalanceRepositoryImpl.class,
    BalanceMapper.class,
    UserMapper.class
})
class SaveBalanceRepositoryImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private SaveBalanceRepositoryImpl saveBalanceRepository;

  private UserDomain user;

  @BeforeEach
  void setUp() {
    UserEntity user = UserFactory.createFakeData();
    this.entityManager.persist(user);
    this.entityManager.flush();
    this.user = this.userMapper.toDomain(user);
  }

  @Test
  @DisplayName("should insert one the user balance record")
  void shouldInsertOneTheUserBalanceRecord() {
    BalanceDomain balanceDomain = new BalanceDomain(BigDecimal.TEN, this.user, (short) 1, (short) 2025);
    BalanceDomain balanceSaved = this.saveBalanceRepository.one(balanceDomain);

    assertNotNull(balanceSaved.getId());
    assertEquals(balanceDomain.getAmount(), balanceSaved.getAmount());
    assertEquals(balanceDomain.getUser().getId(), balanceSaved.getUser().getId());
    assertEquals(balanceDomain.getMonth(), balanceSaved.getMonth());
    assertEquals(balanceDomain.getYear(), balanceSaved.getYear());
  }

  @Test
  @DisplayName("should insert several balance records")
  void all() {
    List<BalanceDomain> balances = List.of(
        new BalanceDomain(BigDecimal.TEN, this.user, (short) 1, (short) 2025),
        new BalanceDomain(BigDecimal.TEN, this.user, (short) 2, (short) 2025),
        new BalanceDomain(BigDecimal.TEN, this.user, (short) 3, (short) 2025),
        new BalanceDomain(BigDecimal.TEN, this.user, (short) 4, (short) 2025)
    );
    List<BalanceDomain> balancesSaved = this.saveBalanceRepository.all(balances);
    assertEquals(4, balancesSaved.size());
    for (BalanceDomain balance : balancesSaved) {
      assertNotNull(balance.getId());
    }
  }
}