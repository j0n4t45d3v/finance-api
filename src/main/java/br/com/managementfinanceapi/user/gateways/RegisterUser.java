package br.com.managementfinanceapi.user.gateways;

import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.domain.dto.CreateUser;

public interface RegisterUser {
    User execute(CreateUser body);
}
