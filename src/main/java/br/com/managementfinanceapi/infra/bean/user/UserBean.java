package br.com.managementfinanceapi.infra.bean.user;

import br.com.managementfinanceapi.application.core.usecase.user.RegisterUserUseCase;
import br.com.managementfinanceapi.application.port.in.user.RegisterUser;
import br.com.managementfinanceapi.application.port.out.user.SaveUser;
import br.com.managementfinanceapi.application.port.out.user.SearchUser;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserBean {

  @Bean
  public RegisterUser registerUser(SaveUser saveUser, SearchUser searchUser) {
    return new RegisterUserUseCase(saveUser, searchUser);
  }

}
