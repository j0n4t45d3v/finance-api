package com.jonatas.finance.auth;

import static com.jonatas.finance.auth.AuthController.RefreshTokenRequest;

import com.jonatas.finance.auth.AuthController.RegisterUserRequest;

public interface AuthService {

  LoginResult login(Email email, String password);

    RefreshTokenResult refresh(RefreshTokenRequest request);

  RegisterResult register(RegisterUserRequest request);

}
