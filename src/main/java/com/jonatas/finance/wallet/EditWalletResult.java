package com.jonatas.finance.wallet;

public sealed interface EditWalletResult
    permits EditWalletResult.Success,
            EditWalletResult.AlreadyExistsMainWalletForUser,
            EditWalletResult.AlreadyExistsWalletWithThisName,
            EditWalletResult.WalletNotFound{

    record Success() implements EditWalletResult {}

    record AlreadyExistsMainWalletForUser() implements EditWalletResult {}

    record AlreadyExistsWalletWithThisName() implements EditWalletResult {}

    record WalletNotFound() implements EditWalletResult {}

}
