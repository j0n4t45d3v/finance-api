package br.com.managementfinanceapi.adapter.out.repository.user.implementation;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@Import({FindUserPortImpl.class, UserMapper.class})
class FindUserPortImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private FindUserPortImpl findUserPort;
  private UserEntity userPersistence;

  @BeforeEach
  void setUp() {
    this.userPersistence = new UserEntity("john@doe.test", "secretpassword");
    this.entityManager.persist(this.userPersistence);
    this.entityManager.flush();
  }

  @Test
  @DisplayName("should return user when exists user with the e-mail")
  void shouldReturnUserWhenExistsUserWithTheEmail() {
    Optional<UserDomain> result = findUserPort.byEmail(this.userPersistence.getEmail());
    assertTrue(result.isPresent());
    assertEquals(this.userPersistence.getId(), result.get().getId());
    assertEquals(this.userPersistence.getEmail(), result.get().getEmail());
    assertEquals(this.userPersistence.getPassword(), result.get().getPassword());
  }

  @Test
  @DisplayName("should return empty user when not exists user with the e-mail")
  void shouldReturnEmptyUserWhenNotExistsUserWithTheEmail() {
    String email = "john@doe.nothing";
    Optional<UserDomain> result = findUserPort.byEmail(email);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("should return user when exists user with the id")
  void shouldReturnUserWhenExistsUserWithTheId() {
    Optional<UserDomain> result = findUserPort.byId(this.userPersistence.getId());
    assertTrue(result.isPresent());
    assertEquals(this.userPersistence.getId(), result.get().getId());
    assertEquals(this.userPersistence.getEmail(), result.get().getEmail());
    assertEquals(this.userPersistence.getPassword(), result.get().getPassword());
  }

  @Test
  @DisplayName("should return empty user when not exists user with the id")
  void shouldReturnEmptyUserWhenNotExistsUserWithTheId() {
    Optional<UserDomain> result = findUserPort.byId(-1L);
    assertTrue(result.isEmpty());
  }
}