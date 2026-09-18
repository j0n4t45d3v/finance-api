package com.jonatas.finance.wallet;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria")
public record CategoryResponse(@Schema(example = "1") Long id,
                               @Schema(example = "Alimentação") String name,
                               @Schema(example = "EXPENSE") String type) {

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getNameValue(), category.getType().name());
    }
}
