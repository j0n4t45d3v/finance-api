package com.management.finance.transaction;

import com.management.finance.transaction.dto.AddTransaction;
import com.management.finance.transaction.dto.EditTransaction;

import java.util.List;

public interface TransactionService {

    void add(Long userId, AddTransaction transactionDTO);
    List<Transaction> getAll(Long userId);
    void deleteById(Long id);
    void editTransaction(Long id, Long userId, EditTransaction transactionEdited);
    byte[] report(Long userId);
}
