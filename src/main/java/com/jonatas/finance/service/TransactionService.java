package com.jonatas.finance.service;

import com.jonatas.finance.domain.Transaction;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateTransactionResult;
import com.jonatas.finance.dto.account.CreateTransactionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    CreateTransactionResult create(CreateTransactionRequest request, User user);

    Page<Transaction> getPage(User user, Pageable pageable);
    
}
