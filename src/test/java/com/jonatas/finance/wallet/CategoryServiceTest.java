package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jonatas.finance.common.exception.DomainException;
import com.jonatas.finance.faker.Faker;

@ExtendWith({ MockitoExtension.class })
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryServiceImpl categoryService;

    @Nested
    class Create {

        @Test
        void shouldCreateCategoryWithSuccess() {
            var category = Faker.category().get();

            when(categoryRepository.findByNameAndUser(category.getName(),
                                                      category.getUser())).thenReturn(Optional.empty());
            when(categoryRepository.save(category)).thenReturn(category);

            assertThatNoException().isThrownBy(() -> {
                var result = categoryService.execute(category);
                assertThat(result)
                                  .isNotNull()
                                  .extracting(Category::getId)
                                  .isEqualTo(category.getId());
            });

            verify(categoryRepository, times(1)).findByNameAndUser(category.getName(), category.getUser());
            verify(categoryRepository, times(1)).save(category);
        }

        @Test
        void shouldNotAllowCreateCategoryWhenAlreadyExistsCategoryWithSameNameForTheUser() {
            var category = Faker.category().get();

            when(categoryRepository.findByNameAndUser(category.getName(),
                                                      category.getUser())).thenReturn(Optional.of(category));

            assertThatExceptionOfType(DomainException.class)
                                                            .isThrownBy(() -> categoryService.execute(category))
                                                            .withMessage("Category already exists");

            verify(categoryRepository, times(1)).findByNameAndUser(category.getName(), category.getUser());
            verify(categoryRepository, never()).save(category);
        }

    }

}