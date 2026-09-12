package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jonatas.finance.common.Result;
import com.jonatas.finance.faker.Faker;

@ExtendWith({ MockitoExtension.class })
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Nested
    class Create {

        @Test
        void shouldCreateCategoryWithSuccess() {
            var category = Faker.category().get();

            when(categoryRepository.findByNameAndUser(category.getName(),
                                                      category.getUser())).thenReturn(Optional.empty());
            when(categoryRepository.save(category)).thenReturn(category);
            var result = categoryService.create(category);
            assertThat(result)
                              .isNotNull()
                              .satisfies(r -> assertThat(r.isFailure()).isFalse())
                              .extracting(Result::get)
                              .isNotNull()
                              .isEqualTo(category);

            verify(categoryRepository, times(1)).findByNameAndUser(category.getName(), category.getUser());
            verify(categoryRepository, times(1)).save(category);
        }

        @Test
        void shouldNotAllowCreateCategoryWhenAlreadyExistsCategoryWithSameNameForTheUser() {
            var category = Faker.category().get();

            when(categoryRepository.findByNameAndUser(category.getName(),
                                                      category.getUser())).thenReturn(Optional.of(category));
            var result = categoryService.create(category);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError()).isEqualTo(CategoryErrorCode.ALREADY_EXISTS_CATEGORY_WITH_NAME);

            verify(categoryRepository, times(1)).findByNameAndUser(category.getName(), category.getUser());
            verify(categoryRepository, never()).save(category);
        }

    }

}