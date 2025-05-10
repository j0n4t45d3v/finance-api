package br.com.managementfinanceapi.adapter.out.repository.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Page<Transaction> findAllByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
  Stream<Transaction> findAllByUserId(Long userId);
}