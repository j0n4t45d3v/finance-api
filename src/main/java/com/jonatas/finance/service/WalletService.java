package com.jonatas.finance.service;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.controller.WalletController.CreateWalletRequest;
import com.jonatas.finance.controller.WalletController.EditWalletRequest;
import com.jonatas.finance.domain.Wallet;
import com.jonatas.finance.domain.result.wallet.CreateWalletResult;
import com.jonatas.finance.domain.result.wallet.EditWalletResult;

import java.util.List;

public interface WalletService {

    CreateWalletResult create(CreateWalletRequest request, User user);

    EditWalletResult update(Long id, EditWalletRequest request, User user);

    List<Wallet> findAll(User user);
}
