package com.jonatas.finance.domain.result.account;

public sealed interface EditAccountResult
    permits EditAccountResult.Success,
            EditAccountResult.AlreadyExistsMainAccountForUser,
            EditAccountResult.AlreadyExistsAccountWithThisName,
            EditAccountResult.AccountNotFound{

    record Success() implements EditAccountResult {}

    record AlreadyExistsMainAccountForUser() implements EditAccountResult {}

    record AlreadyExistsAccountWithThisName() implements EditAccountResult {}

    record AccountNotFound() implements EditAccountResult {}

}
