package com.jonatas.finance.adapter.security;

import com.jonatas.finance.auth.User;

public interface TokenProvider {

    PairToken generatePairToken(User user);

    DecodedToken validateAccessToken(String token);

    DecodedToken validateRefreshToken(String token);

}
