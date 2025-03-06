package br.com.managementfinanceapi.user.gateways;

import br.com.managementfinanceapi.user.domain.dto.CreateUser;
import br.com.managementfinanceapi.user.domain.dto.UserResponse;

public interface RegisterUser {
    UserResponse execute(CreateUser body);
}
