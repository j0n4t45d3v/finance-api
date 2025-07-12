package br.com.managementfinanceapi.adapter.out.repository.category.implementation;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.category.CategoryMapper;
import br.com.managementfinanceapi.adapter.out.mapper.user.UserMapper;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@Import({
    SaveCategoryRepositoryImpl.class,
    CategoryMapper.class,
    UserMapper.class
})
class SaveCategoryRepositoryImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private SaveCategoryRepositoryImpl saveCategoryRepository;
  private UserDomain user;

  @BeforeEach
  void setUp(){
    UserEntity user = UserFactory.createFakeData();
    this.entityManager.persist(user);
    this.entityManager.flush();
    this.user = this.userMapper.toDomain(user);
  }

  @Test
  @DisplayName("should insert a new user category")
  void shouldInsertANewUserCategory() {
    CategoryDomain categoryDomain = new CategoryDomain(null, "Test Insert", BigDecimal.TEN, this.user);
    CategoryDomain categoryInserted = this.saveCategoryRepository.execute(categoryDomain);
    String query = "select * from categories where name = :name";
    CategoryEntity categoryResult = (CategoryEntity) this.entityManager
        .createNativeQuery(query, CategoryEntity.class)
        .setParameter("name", categoryDomain.getName())
        .getSingleResult();

    assertNotNull(categoryInserted);
    assertEquals(categoryResult.getId(), categoryInserted.getId());
    assertEquals(categoryDomain.getName(), categoryInserted.getName());
    assertEquals(categoryDomain.getCreditLimit(), categoryInserted.getCreditLimit());
  }

}