package br.com.managementfinanceapi.transaction.repository;

import br.com.managementfinanceapi.transaction.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

  Stream<Balance> findAllByUserId(Long userId);

  Optional<Balance> findByUserIdAndMonthAndYear(Long userId, short month, short year);
  List<Balance> findAllBalancesWithMonthANdYearGreater(
      @Param("month") short month,
      @Param("year") short year,
      @Param("userId") Long userId
  );
}
