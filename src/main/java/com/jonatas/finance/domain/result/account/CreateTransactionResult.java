package com.jonatas.finance.domain.result.account;

import com.jonatas.finance.domain.Transaction;

public sealed interface CreateTransactionResult
    permits CreateTransactionResult.Success,
            CreateTransactionResult.CategoryNotFound,
            CreateTransactionResult.AccountNotFound,
            CreateTransactionResult.TransactionCannotBeIsInTheFuture{

    record Success(Transaction transaction) implements CreateTransactionResult {
    }

    record CategoryNotFound() implements CreateTransactionResult {
    }

    record AccountNotFound() implements CreateTransactionResult {
    }

    record TransactionCannotBeIsInTheFuture() implements CreateTransactionResult {
    }

}
