package com.jonatas.finance.domain.result.wallet;

import com.jonatas.finance.domain.Wallet;

public sealed interface CreateWalletResult
    permits CreateWalletResult.Success,
            CreateWalletResult.AlreadyExistsWalletWithThisName,
            CreateWalletResult.AlreadyExistsMainWalletForUser {

    record Success(Wallet wallet) implements CreateWalletResult {}

    record AlreadyExistsWalletWithThisName() implements CreateWalletResult {}

    record AlreadyExistsMainWalletForUser() implements CreateWalletResult {}

}
