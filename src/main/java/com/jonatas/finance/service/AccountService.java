package com.jonatas.finance.service;

import com.jonatas.finance.controller.AccountController;
import com.jonatas.finance.controller.AccountController.CreateAccountRequest;
import com.jonatas.finance.controller.TransactionController.AddTransactionRequest;
import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateAccountResult;
import com.jonatas.finance.domain.result.account.CreateTransactionResult;

import java.util.List;

public interface AccountService {

    CreateAccountResult create(CreateAccountRequest request, User user);

    List<Account> findAll(User user);
}
