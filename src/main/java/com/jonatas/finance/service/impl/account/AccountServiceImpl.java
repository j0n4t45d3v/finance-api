package com.jonatas.finance.service.impl.account;

import com.jonatas.finance.controller.AccountController.CreateAccountRequest;
import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.Account.Description;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateAccountResult;
import com.jonatas.finance.repository.AccountRepository;
import com.jonatas.finance.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public CreateAccountResult create(CreateAccountRequest request, User user) {
        Description accountName = new Description(request.name());
        if (this.alreadyExistsUserAccountWithName(user, accountName)) {
            return new CreateAccountResult.AlreadyExistsAccountWithThisName();
        }

        if (request.mainAccount() && this.alreadyExistsUserMainAccount(user)) {
            return new CreateAccountResult.AlreadyExistsMainAccountForUser();
        }

        Account accountCreated = this.accountRepository.save(new Account(accountName, user, request.mainAccount()));
        return new CreateAccountResult.Success(accountCreated);
    }

    private boolean alreadyExistsUserAccountWithName(User user, Description accountName) {
        return this.accountRepository.existsByDescriptionAndUser(accountName, user);
    }

    private boolean alreadyExistsUserMainAccount(User user) {
        return this.accountRepository.existsMainAccountForUser(user);
    }

    @Override
    public List<Account> findAll(User user) {
        return this.accountRepository.findAllByUser(user);
    }

}
