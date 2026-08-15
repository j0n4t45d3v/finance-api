package com.jonatas.finance.wallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jonatas.finance.auth.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByUser(User user, Pageable pageable);
}
