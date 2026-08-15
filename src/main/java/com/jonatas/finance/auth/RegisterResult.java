package com.jonatas.finance.auth;

public sealed interface RegisterResult 
    permits RegisterResult.Success,
            RegisterResult.FailRegister,
            RegisterResult.NotMatchPasswords{

    record Success() implements RegisterResult {}

    record FailRegister() implements RegisterResult {}

    record NotMatchPasswords() implements RegisterResult {}

}
