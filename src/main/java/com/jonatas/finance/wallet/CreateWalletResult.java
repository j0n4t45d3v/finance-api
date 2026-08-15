package com.jonatas.finance.wallet;

public sealed interface CreateWalletResult
    permits CreateWalletResult.Success,
            CreateWalletResult.AlreadyExistsWalletWithThisName,
            CreateWalletResult.AlreadyExistsMainWalletForUser {

    record Success(Wallet wallet) implements CreateWalletResult {}

    record AlreadyExistsWalletWithThisName() implements CreateWalletResult {}

    record AlreadyExistsMainWalletForUser() implements CreateWalletResult {}

}
