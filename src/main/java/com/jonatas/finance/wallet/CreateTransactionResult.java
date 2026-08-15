package com.jonatas.finance.wallet;

public sealed interface CreateTransactionResult
    permits CreateTransactionResult.Success,
            CreateTransactionResult.CategoryNotFound,
            CreateTransactionResult.WalletNotFound,
            CreateTransactionResult.TransactionCannotBeIsInTheFuture{

    record Success(Transaction transaction) implements CreateTransactionResult {
    }

    record CategoryNotFound() implements CreateTransactionResult {
    }

    record WalletNotFound() implements CreateTransactionResult {
    }

    record TransactionCannotBeIsInTheFuture() implements CreateTransactionResult {
    }

}
