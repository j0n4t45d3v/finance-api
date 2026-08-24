package com.jonatas.finance.auth;

import com.jonatas.finance.common.dto.Token;

public sealed interface RefreshTokenResult
    permits RefreshTokenResult.Success,
        RefreshTokenResult.InvalidRefreshToken,
        RefreshTokenResult.InvalidSubject {

  record Success(Token access, Token refresh) implements RefreshTokenResult {}

  record InvalidRefreshToken() implements RefreshTokenResult {}

  record InvalidSubject() implements RefreshTokenResult {}
}
