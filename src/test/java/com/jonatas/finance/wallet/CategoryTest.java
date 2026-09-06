package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.exception.DomainException;
import com.jonatas.finance.faker.Faker;
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
        var categoryFaker = Faker.category().withType(type);
        assertThatNoException().isThrownBy(categoryFaker::get);
        assertThat(categoryFaker.get()).satisfies(
                c -> {
                    assertThat(c.getId()).isNotNull();
                    assertThat(c.getName()).isNotNull();
                    assertThat(c.getType()).isEqualTo(type);
                    assertThat(c.getUser()).isNotNull();
                });
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("providerNullFieldsRequired")
    void shouldThrowExceptionWhenNotGivenRequiredFields(
                                                        String scenery, String name, Type type, User user) {
        var categoryFaker = Faker.category().withName(name).withUser(user).withType(type);
        Assertions.assertThatNullPointerException().isThrownBy(() -> categoryFaker.get()).withMessageContainingAll("is required");
    }

    public static Stream<Arguments> providerNullFieldsRequired() {
        var user = Faker.user().get();
        return Stream.of(
                Arguments.arguments("Name is null", null, Type.EXPENSE, user), Arguments.arguments("Type is null", Faker.text(10), null, user), Arguments.arguments("User is null", Faker.text(2), Type.EXPENSE, null));
    }

    @Nested
    class NameTest {
        @Test
        void shouldInstanceName() {
            assertThatNoException().isThrownBy(() -> Name.of(Faker.text(10)));
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldThrowExceptionWhenGivenInvalidValue(String rawName) {
            assertThatException().isThrownBy(() -> Name.of(rawName)).isExactlyInstanceOf(DomainException.class);
        }
    }
}
