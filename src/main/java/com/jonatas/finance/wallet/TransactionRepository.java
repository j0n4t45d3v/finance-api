package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Page<Transaction> findAllByUser(User user, Pageable pageable);
}
