package br.com.managementfinanceapi.movimentation.repository;

import br.com.managementfinanceapi.movimentation.domain.Transaction;

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