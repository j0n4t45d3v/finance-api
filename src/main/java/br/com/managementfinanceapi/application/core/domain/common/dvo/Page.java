package br.com.managementfinanceapi.application.core.domain.common.dvo;

import java.util.List;

public record Page<T>(
    List<T> content,
    int page,
    int size,
    int total
) {

  public boolean isEmptyContent() {
    return this.content.isEmpty();
  }
}