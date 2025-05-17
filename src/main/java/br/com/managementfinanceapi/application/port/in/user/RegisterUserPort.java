package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public interface RegisterUser {
    UserDomain execute(UserDomain body, String confirmPassword);
}
