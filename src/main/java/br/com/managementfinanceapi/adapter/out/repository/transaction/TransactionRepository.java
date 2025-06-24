package br.com.managementfinanceapi.adapter.out.repository.transaction;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  Page<TransactionEntity> findAllByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
  List<TransactionEntity> findAllByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
  Page<TransactionEntity> findAllByUserId(Long userId, Pageable pageable);
}