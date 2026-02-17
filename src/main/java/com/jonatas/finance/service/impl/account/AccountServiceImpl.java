package com.jonatas.finance.service.impl.account;

import com.jonatas.finance.controller.AccountController.CreateAccountRequest;
import com.jonatas.finance.controller.AccountController.EditAccountRequest;
import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.Account.Description;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateAccountResult;
import com.jonatas.finance.domain.result.account.EditAccountResult;
import com.jonatas.finance.repository.AccountRepository;
import com.jonatas.finance.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        if (request.mainAccount() && this.alreadyExistsMainAccountForThisUser(user)) {
            return new CreateAccountResult.AlreadyExistsMainAccountForUser();
        }

        Account accountCreated = this.accountRepository.save(new Account(accountName, user, request.mainAccount()));
        return new CreateAccountResult.Success(accountCreated);
    }

    private boolean alreadyExistsUserAccountWithName(User user, Description accountName) {
        return this.accountRepository.existsByDescriptionAndUser(accountName, user);
    }

    private boolean alreadyExistsMainAccountForThisUser(User user) {
        return this.accountRepository.existsMainAccountForUser(user);
    }


    @Override
    public EditAccountResult update(Long id, EditAccountRequest request, User user) {
        Optional<Account> accountFound = this.accountRepository.findByIdAndUser(id, user);
        if (accountFound.isEmpty()) {
            return new EditAccountResult.AccountNotFound();
        }

        if (request.mainAccount() && this.accountRepository.existsMainAccountForUser(user, id)) {
            return new EditAccountResult.AlreadyExistsMainAccountForUser();
        }

        Description accountName = new Description(request.name());
        if (this.accountRepository.existsByDescriptionAndUserNotAndId(accountName, user, id)) {
            return new EditAccountResult.AlreadyExistsAccountWithThisName();
        }

        Account account = accountFound.get();
        account.setMain(request.mainAccount());
        account.setDescription(accountName);
        this.accountRepository.save(account);
        return new EditAccountResult.Success();
    }

    @Override
    public List<Account> findAll(User user) {
        return this.accountRepository.findAllByUser(user);
    }

}
