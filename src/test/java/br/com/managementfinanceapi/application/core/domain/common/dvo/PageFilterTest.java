package br.com.managementfinanceapi.application.core.domain.common.dvo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageFilterTest {

  private static final PageFilter PAGE_FILTER = new PageFilter(10, 2);
  private static final PageFilter PAGER_FILTER_EMPTY = new PageFilter(null, null);

  @Test
  @DisplayName("should return page index and size page when page values is defined")
  void shouldReturnPageIndexWhenPageValueIsDefined() {
    assertEquals(2, PAGE_FILTER.page());
    assertEquals(10, PAGE_FILTER.size());
  }

  @Test
  @DisplayName("should return default page index and size page when page values does not is defined")
  void shouldReturnDefaultPageIndexWhenPageValuesDoesNotIsDefined() {
    assertEquals(0, PAGER_FILTER_EMPTY.page());
    assertEquals(20, PAGER_FILTER_EMPTY.size());
  }

}