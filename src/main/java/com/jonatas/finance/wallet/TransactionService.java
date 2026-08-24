package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

  CreateTransactionResult create(CreateTransactionRequest request, User user);

  Page<Transaction> getPage(User user, Pageable pageable);
}
