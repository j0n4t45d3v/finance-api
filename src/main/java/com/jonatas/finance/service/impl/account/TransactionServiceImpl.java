package com.jonatas.finance.service.impl.account;

import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.result.account.CreateTransactionResult;
import com.jonatas.finance.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jonatas.finance.controller.TransactionController.AddTransactionRequest;
import com.jonatas.finance.domain.Transaction;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.Transaction.Amount;
import com.jonatas.finance.domain.Transaction.Timestamp;
import com.jonatas.finance.repository.CategoryRepository;
import com.jonatas.finance.repository.TransactionRepository;
import com.jonatas.finance.service.TransactionService;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionServiceImpl(
        AccountRepository accountRepository,
        TransactionRepository transactionRepository,
        CategoryRepository categoryRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CreateTransactionResult create(AddTransactionRequest request, User user) {
        Optional<Category> category = this.categoryRepository.findById(request.categoryId());
        if (category.isEmpty()) {
            return new CreateTransactionResult.CategoryNotFound();
        }

        Optional<Account> account = this.accountRepository.findById(request.accountId());
        if (account.isEmpty()) {
            return new CreateTransactionResult.AccountNotFound();
        }

        Transaction transaction = new Transaction(
            new Amount(request.amount()),
            new Timestamp(request.datetime()),
            account.get(),
            user,
            category.get()
        );

        this.transactionRepository.save(transaction);
        return new CreateTransactionResult.Success();
    }

    @Override
    public Page<Transaction> getPage(User user, Pageable pageable) {
        return this.transactionRepository.findAllByUser(user, pageable);
    }

}
