package br.com.managementfinanceapi.adapter.out.repository.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

  Stream<Balance> findAllByUserId(Long userId);

  Optional<Balance> findByUserIdAndMonthAndYear(Long userId, short month, short year);

  @Query(name = "Balance.findAllBalancesWithMonthANdYearGreater")
  List<Balance> findAllBalancesWithMonthANdYearGreater(
      @Param("month") short month,
      @Param("year") short year,
      @Param("userId") Long userId
  );
}
