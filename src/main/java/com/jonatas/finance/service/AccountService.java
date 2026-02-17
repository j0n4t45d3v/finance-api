package com.jonatas.finance.service;

import com.jonatas.finance.controller.AccountController.CreateAccountRequest;
import com.jonatas.finance.controller.AccountController.EditAccountRequest;
import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateAccountResult;
import com.jonatas.finance.domain.result.account.EditAccountResult;

import java.util.List;

public interface AccountService {

    CreateAccountResult create(CreateAccountRequest request, User user);

    EditAccountResult update(Long id, EditAccountRequest request, User user);

    List<Account> findAll(User user);
}
