package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.wallet.WalletController.CreateWalletRequest;
import com.jonatas.finance.wallet.WalletController.EditWalletRequest;
import java.util.List;

public interface WalletService {

  CreateWalletResult create(CreateWalletRequest request, User user);

  EditWalletResult update(Long id, EditWalletRequest request, User user);

  List<Wallet> findAll(User user);
}
