package com.jonatas.finance.domain.result.auth;

import com.jonatas.finance.dto.Token;

public sealed interface RefreshTokenResult
    permits RefreshTokenResult.Success,
    RefreshTokenResult.InvalidRefreshToken,
    RefreshTokenResult.InvalidSubject {

    record Success(Token access, Token refresh) implements RefreshTokenResult {}

    record InvalidRefreshToken() implements RefreshTokenResult {}

    record InvalidSubject() implements RefreshTokenResult {}
}
