package br.com.managementfinanceapi.movimentation.repository;

import br.com.managementfinanceapi.movimentation.domain.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface MovementRepository extends JpaRepository<Transaction, Long> {
  Page<Transaction> findAllByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}