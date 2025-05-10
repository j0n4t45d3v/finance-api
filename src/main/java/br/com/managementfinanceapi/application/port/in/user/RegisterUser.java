package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.User;
import br.com.managementfinanceapi.application.core.domain.user.dto.CreateUser;

public interface RegisterUser {
    User execute(CreateUser body);
}
