package com.jonatas.finance.domain.result.account;

public sealed interface CreateTransactionResult
    permits CreateTransactionResult.Success,
            CreateTransactionResult.CategoryNotFound,
            CreateTransactionResult.AccountNotFound,
            CreateTransactionResult.TransactionCannotBeIsInTheFuture{

    record Success() implements CreateTransactionResult {
    }

    record CategoryNotFound() implements CreateTransactionResult {
    }

    record AccountNotFound() implements CreateTransactionResult {
    }

    record TransactionCannotBeIsInTheFuture() implements CreateTransactionResult {
    }

}
