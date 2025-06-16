package br.com.managementfinanceapi.application.core.domain.common.dvo;

public record PageFilter(
  Integer size,
  Integer page
) { 

  @Override
  public Integer size() {
    if (this.size == null) return 20;
    return this.size;
  }

  @Override
  public Integer page() {
    if (this.page == null) return 0;
    return this.page;
  }
}