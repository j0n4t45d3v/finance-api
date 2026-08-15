package com.jonatas.finance.domain.result.wallet;

import com.jonatas.finance.domain.Transaction;

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
