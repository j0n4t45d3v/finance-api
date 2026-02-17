package com.jonatas.finance.service;

import com.jonatas.finance.controller.AuthController.RegisterUserRequest;
import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.result.auth.LoginResult;
import com.jonatas.finance.domain.result.auth.RefreshTokenResult;
import com.jonatas.finance.domain.result.auth.RegisterResult;

import static com.jonatas.finance.controller.AuthController.RefreshTokenRequest;

public interface AuthService {

  LoginResult login(Email email, String password);

    RefreshTokenResult refresh(RefreshTokenRequest request);

  RegisterResult register(RegisterUserRequest request);

}
