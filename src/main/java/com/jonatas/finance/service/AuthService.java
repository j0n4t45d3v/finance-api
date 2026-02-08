package com.jonatas.finance.service;

import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.result.auth.LoginResult;

public interface AuthService {

  LoginResult login(Email email, String password);

}
