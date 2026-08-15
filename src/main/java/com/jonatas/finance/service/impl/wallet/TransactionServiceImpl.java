package com.jonatas.finance.service.impl.wallet;

import com.jonatas.finance.domain.Wallet;
import com.jonatas.finance.auth.User;
import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.Transaction.Description;
import com.jonatas.finance.domain.result.wallet.CreateTransactionResult;
import com.jonatas.finance.dto.wallet.CreateTransactionRequest;
import com.jonatas.finance.infra.provider.ClockProvider;
import com.jonatas.finance.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jonatas.finance.domain.Transaction;
import com.jonatas.finance.domain.Transaction.Amount;
import com.jonatas.finance.domain.Transaction.Timestamp;
import com.jonatas.finance.repository.CategoryRepository;
import com.jonatas.finance.repository.TransactionRepository;
import com.jonatas.finance.service.TransactionService;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final ClockProvider clockProvider;

    public TransactionServiceImpl(
        WalletRepository walletRepository,
        TransactionRepository transactionRepository,
        CategoryRepository categoryRepository,
        ClockProvider clockProvider
    ) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.clockProvider = clockProvider;
    }

    @Override
    @Transactional
    public CreateTransactionResult create(CreateTransactionRequest request, User user) {
        Optional<Category> category = this.categoryRepository.findByIdAndUser(request.categoryId(), user);
        if (category.isEmpty()) {
            return new CreateTransactionResult.CategoryNotFound();
        }

        Optional<Wallet> wallet = this.walletRepository.findByIdAndUser(request.walletId(), user);
        if (wallet.isEmpty()) {
            return new CreateTransactionResult.WalletNotFound();
        }

        if (request.datetime().isAfter(this.clockProvider.now())) {
            return new CreateTransactionResult.TransactionCannotBeIsInTheFuture();
        }

        Transaction transaction = new Transaction(
            new Description(request.description()),
            new Amount(request.amount()),
            new Timestamp(request.datetime()),
            wallet.get(),
            user,
            category.get()
        );

        Transaction created = this.transactionRepository.save(transaction);
        return new CreateTransactionResult.Success(created);
    }

    @Override
    public Page<Transaction> getPage(User user, Pageable pageable) {
        return this.transactionRepository.findAllByUser(user, pageable);
    }

}
