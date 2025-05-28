package br.com.managementfinanceapi.adapter.out.repository.transaction;

import br.com.managementfinanceapi.adapter.out.entity.transaction.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

  List<BalanceEntity> findAllByUserId(Long userId);

  Optional<BalanceEntity> findByUserIdAndMonthAndYear(Long userId, short month, short year);

  @Query(name = "BalanceEntity.findAllBalancesAfterMonthYear")
  List<BalanceEntity> findAllBalancesAfterMonthYear(
      @Param("month") short month,
      @Param("year") short year,
      @Param("userId") Long userId
  );
}