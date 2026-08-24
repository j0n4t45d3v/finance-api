package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.infra.provider.ClockProvider;
import com.jonatas.finance.wallet.Transaction.Amount;
import com.jonatas.finance.wallet.Transaction.Description;
import com.jonatas.finance.wallet.Transaction.Timestamp;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
      ClockProvider clockProvider) {
    this.walletRepository = walletRepository;
    this.transactionRepository = transactionRepository;
    this.categoryRepository = categoryRepository;
    this.clockProvider = clockProvider;
  }

  @Override
  @Transactional
  public CreateTransactionResult create(CreateTransactionRequest request, User user) {
    Optional<Category> category =
        this.categoryRepository.findByIdAndUser(request.categoryId(), user);
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

    Transaction transaction =
        new Transaction(
            new Description(request.description()),
            new Amount(request.amount()),
            new Timestamp(request.datetime()),
            wallet.get(),
            user,
            category.get());

    Transaction created = this.transactionRepository.save(transaction);
    return new CreateTransactionResult.Success(created);
  }

  @Override
  public Page<Transaction> getPage(User user, Pageable pageable) {
    return this.transactionRepository.findAllByUser(user, pageable);
  }
}
