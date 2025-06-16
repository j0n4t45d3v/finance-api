package br.com.managementfinanceapi.adapter.in.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateCategory(
    @NotNull(message = "O campo 'user_id' é obrigatório")
    @JsonProperty("user_id")
    Long userId,
    @NotNull(message = "O campo 'name' é obrigatório")
    String name,
    @JsonProperty("credit_limit")
    BigDecimal creditLimit
) {

  public CategoryDomain toDomain() {
    return new CategoryDomain(null, this.name, this.creditLimit, new UserDomain(this.userId));
  }

}