package com.management.finance.transaction.impl;

import com.management.finance.error.exceptions.BadRequestException;
import com.management.finance.error.exceptions.InternalServerErrorException;
import com.management.finance.error.exceptions.NotFoundException;
import com.management.finance.transaction.Transaction;
import com.management.finance.transaction.TransactionRepository;
import com.management.finance.transaction.TransactionService;
import com.management.finance.transaction.dto.AddTransaction;
import com.management.finance.transaction.dto.EditTransaction;
import com.management.finance.user.User;
import com.management.finance.user.UserService;
import com.management.finance.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private final UserService userService;

    @Override
    public void add(Long userId, AddTransaction transactionDTO) {
        this.userNotExists(userId);
        Transaction transaction = this.mapToEntity(transactionDTO);
        User user = new User();
        user.setId(userId);
        transaction.setUser(user);
        this.transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAll(Long userId) {
        this.userNotExists(userId);
        return this.transactionRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        boolean exists = this.transactionRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException("Transaction not found");
        }
        this.transactionRepository.deleteById(id);
    }

    @Override
    public void editTransaction(Long id, Long userId, EditTransaction transactionEdited) {
        this.userNotExists(userId);
        Transaction transactionFounded = this.transactionRepository.findById(id)
                .orElseThrow(this::transactionNotFound);

        if (!transactionFounded.getUser().getId().equals(userId))
            throw new BadRequestException("Transaction does not belong to the user");
        }
        existingTransaction.setAmount(transactionEdited.amount());
        existingTransaction.setType(transactionEdited.type());
        this.transactionRepository.save(existingTransaction);
    }

    @Override
    public byte[] report(Long userId) {
        return new byte[0];
    }

    private void userNotExists(Long userId) {
        this.userService.getById(userId);
    }

    private Transaction mapToEntity(Record transactionDTO) {
        return this.mapper
                .recordToEntity(transactionDTO, Transaction.class)
                .orElseThrow(() -> new InternalServerErrorException("Mapping failed"));
    }
}
