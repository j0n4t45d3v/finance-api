package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição de criação de uma categoria")
public record CreateCategoryRequest(@Schema(example = "Alimentação") @NotBlank(message = "name is required") @Size(min = 5, max = 50, message = "name must be between 5 and 50 characters") String name,
                                    @Schema(example = "EXPENSE") @NotNull(message = "type is required") Category.Type type) {

    public Category toEntity(User user) {
        return new Category(Category.Name.of(this.name), this.type, user);
    }
}
