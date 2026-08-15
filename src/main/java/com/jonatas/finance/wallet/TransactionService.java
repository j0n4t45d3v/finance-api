package com.jonatas.finance.wallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jonatas.finance.auth.User;

public interface TransactionService {

    CreateTransactionResult create(CreateTransactionRequest request, User user);

    Page<Transaction> getPage(User user, Pageable pageable);
    
}
