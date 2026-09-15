package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.adapter.time.ClockProvider;
import com.jonatas.finance.common.Result;
import com.jonatas.finance.wallet.Transaction.Amount;
import com.jonatas.finance.wallet.Transaction.Description;
import com.jonatas.finance.wallet.Transaction.Timestamp;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final ClockProvider clockProvider;

    public TransactionService(WalletRepository walletRepository,
                              TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              ClockProvider clockProvider) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.clockProvider = clockProvider;
    }

    @Transactional
    public Result<Transaction> create(CreateTransactionRequest request, User user) {
        Optional<Category> category = this.categoryRepository.findByIdAndUser(request.categoryId(), user);
        if (category.isEmpty()) {
            return Result.failure(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }

        Optional<Wallet> wallet = this.walletRepository.findByIdAndUser(request.walletId(), user);
        if (wallet.isEmpty()) {
            return Result.failure(WalletErrorCode.WALLET_NOT_FOUND);
        }

        if (request.datetime().isAfter(this.clockProvider.now())) {
            return Result.failure(TransactionErrorCode.TRANSACTION_CANNOT_BE_CREATED_IN_THE_FUTURE);
        }

        Transaction transaction = new Transaction(new Description(request.description()),
                                                  new Amount(request.amount()),
                                                  new Timestamp(request.datetime()),
                                                  wallet.get(),
                                                  user,
                                                  category.get());

        Transaction created = this.transactionRepository.save(transaction);
        return Result.success(created);
    }

    public Page<Transaction> getPage(User user, Pageable pageable) {
        return this.transactionRepository.findAllByUser(user, pageable);
    }
}
