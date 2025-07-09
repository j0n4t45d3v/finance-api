package br.com.managementfinanceapi.adapter.out.repository.user.implementation;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@Import({
    SaveUsePortImpl.class,
    UserMapper.class
})
class SaveUsePortImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private SaveUsePortImpl saveUserPort;

  @Test
  @DisplayName("should insert a new user in database")
  void shouldInsertANewUserInDatabase() {
    String email = "john@doe.test";
    UserDomain userDomain = new UserDomain(email, Password.fromEncoded("secretPassword"));
    UserDomain userInserted = this.saveUserPort.execute(userDomain);

    String query = "select * from users where email = :email ";
    UserEntity userResulted = (UserEntity) this.entityManager
        .createNativeQuery(query, UserEntity.class)
        .setParameter("email", email)
        .getSingleResult();

    assertNotNull(userInserted.getId());
    assertEquals(userResulted.getId(), userInserted.getId());
    assertEquals(userDomain.getEmail(), userInserted.getEmail());
    assertEquals(userDomain.getPassword(), userInserted.getPassword());
  }

}