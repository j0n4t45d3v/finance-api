package com.jonatas.finance.wallet;

import static org.mockito.Mockito.mock;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.exception.DomainException;
import com.jonatas.finance.wallet.Category.Name;
import com.jonatas.finance.wallet.Category.Type;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class CategoryTest {

  @ParameterizedTest
  @EnumSource(names = {"EXPENSE", "INCOME"})
  void shouldCreateACategory(Type type) {
    var user = mock(User.class);

    Assertions.assertThatNoException()
        .isThrownBy(() -> new Category(Name.of("category"), type, user));
  }

  @ParameterizedTest
  @MethodSource("providerNullFieldsRequired")
  void shouldThrowExceptionWhenNotGivenRequiredFields(Name name, Type type, User user) {
    Assertions.assertThatNullPointerException().isThrownBy(() -> new Category(name, type, user));
  }

  public static Stream<Arguments> providerNullFieldsRequired() {
    return Stream.of(
        Arguments.arguments(null, Type.EXPENSE, mock(User.class)),
        Arguments.arguments(Name.of("Category"), null, mock(User.class)),
        Arguments.arguments(Name.of("Category"), Type.EXPENSE, null));
  }

  @Nested
  class NameTest {

    @Test
    void shouldInstanceName() {
      Assertions.assertThatNoException().isThrownBy(() -> Name.of("category"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenGivenInvalidValue(String rawName) {
      Assertions.assertThatException()
          .isThrownBy(() -> Name.of(rawName))
          .isExactlyInstanceOf(DomainException.class);
    }
  }
}
