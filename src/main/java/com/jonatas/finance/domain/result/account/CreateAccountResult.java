package com.jonatas.finance.domain.result.account;

import com.jonatas.finance.domain.Account;

public sealed interface CreateAccountResult
    permits CreateAccountResult.Success,
            CreateAccountResult.AlreadyExistsAccountWithThisName,
            CreateAccountResult.AlreadyExistsMainAccountForUser {

    record Success(Account account) implements CreateAccountResult {}

    record AlreadyExistsAccountWithThisName() implements CreateAccountResult {}

    record AlreadyExistsMainAccountForUser() implements CreateAccountResult {}

}
