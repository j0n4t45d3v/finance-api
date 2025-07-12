package br.com.managementfinanceapi.adapter.out.repository.category.implementation;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.config.PostgreSQLTestContainer;
import br.com.managementfinanceapi.factory.CategoryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@Import(ExistsCategoryRepositoryImpl.class)
class ExistsCategoryRepositoryImplTest extends PostgreSQLTestContainer {

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private ExistsCategoryRepositoryImpl existsCategoryRepository;
  private CategoryEntity category;

  @BeforeEach
  void setUp() {
    this.category = CategoryFactory.createFakeData();
    this.entityManager.persist(this.category.getUser());
    this.entityManager.persist(this.category);
    this.entityManager.flush();
  }
  @Test
  @DisplayName("should return true when exists user category with the id")
  void shouldReturnTrueWhenExistsUserCategoryWithTheId() {
    boolean exists = this.existsCategoryRepository.byId(this.category.getId());
    assertTrue(exists);
  }

  @Test
  @DisplayName("should return true when exists user category with the name")
  void shouldReturnTrueWhenExistsUserCategoryWithTheName() {
    boolean exists = this.existsCategoryRepository.byName(this.category.getName());
    assertTrue(exists);
  }
}