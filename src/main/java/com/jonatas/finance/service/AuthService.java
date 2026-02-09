package com.jonatas.finance.service;

import com.jonatas.finance.controller.AuthController.RegisterUserRequest;
import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.result.auth.LoginResult;
import com.jonatas.finance.domain.result.auth.RegisterResult;

public interface AuthService {

  LoginResult login(Email email, String password);

  RegisterResult register(RegisterUserRequest request);

}
