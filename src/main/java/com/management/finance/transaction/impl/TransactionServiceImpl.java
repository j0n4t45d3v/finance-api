package com.management.finance.transaction.impl;

import com.management.finance.transaction.Transaction;
import com.management.finance.transaction.TransactionRepository;
import com.management.finance.transaction.TransactionService;
import com.management.finance.transaction.dto.AddTransaction;
import com.management.finance.transaction.dto.EditTransaction;
import com.management.finance.user.User;
import com.management.finance.user.UserRepository;
import com.management.finance.user.impl.UserServiceImpl;
import com.management.finance.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private final UserRepository userRepository;

    @Override
    public void add(Long userId, AddTransaction transactionDTO) {
        boolean userExists = this.userRepository.existsById(userId);
        if(userExists) throw new RuntimeException(UserServiceImpl.NOT_FOUND_MESSAGE);
        Transaction transaction = this.mapToEntity(transactionDTO);
        User user = new User();
        user.setId(userId);
        transaction.setUser(user);
        this.transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAll(Long userId) {
        boolean userExists = this.userRepository.existsById(userId);
        if (!userExists) {
            throw new RuntimeException(UserServiceImpl.NOT_FOUND_MESSAGE);
        }
        return this.transactionRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        boolean exists = this.transactionRepository.existsById(id);
        if (!exists) {
            throw new RuntimeException("Transaction not found");
        }
        this.transactionRepository.deleteById(id);
    }

    @Override
    public void editTransaction(Long id, Long userId, EditTransaction transactionEdited) {
        boolean userExists = this.userRepository.existsById(userId);
        if (!userExists) throw new RuntimeException(UserServiceImpl.NOT_FOUND_MESSAGE);

        Transaction existingTransaction = this.transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!existingTransaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("Transaction does not belong to the user");
        }
        existingTransaction.setAmount(transactionEdited.amount());
        existingTransaction.setType(transactionEdited.type());
        this.transactionRepository.save(existingTransaction);
    }

    @Override
    public byte[] report(Long userId) {
        return new byte[0];
    }

    private Transaction mapToEntity(Record transactionDTO) {
        return this.mapper
                .recordToEntity(transactionDTO, Transaction.class)
                .orElseThrow(() -> new RuntimeException("Mapping failed"));
    }
}
