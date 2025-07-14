package br.com.managementfinanceapi.adapter.out.repository.transaction.specifications;

import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecifications {

  public static Specification<TransactionEntity> hasUser(long userId) {
    return (root, query, criteriaBuilder) ->
        root.join("user").get("id").in(userId);
  }

  public static Specification<TransactionEntity> inWithinDateRange(DateRange range) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.between(root.get("date"), range.startWithTime(), range.endWithTime());
  }

}
