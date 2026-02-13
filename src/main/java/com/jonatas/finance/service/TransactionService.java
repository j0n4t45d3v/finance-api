package com.jonatas.finance.service;

import com.jonatas.finance.controller.TransactionController.AddTransactionRequest;
import com.jonatas.finance.domain.Transaction;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateTransactionResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    CreateTransactionResult create(AddTransactionRequest request, User user);

    Page<Transaction> getPage(User user, Pageable pageable);
    
}
