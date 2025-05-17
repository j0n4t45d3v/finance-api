package br.com.managementfinanceapi.infra.bean.user;

import br.com.managementfinanceapi.application.core.usecase.user.RegisterUserUseCase;
import br.com.managementfinanceapi.application.core.usecase.user.SearchUseCase;
import br.com.managementfinanceapi.application.port.in.user.RegisterUserPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserBean {

  @Bean
  public RegisterUserPort registerUser(SaveUserPort saveUserPort, FindUserPort findUserPort) {
    return new RegisterUserUseCase(saveUserPort, findUserPort);
  }
  @Bean
  public SearchUserPort searchUser(FindUserPort findUserPort) {
    return new SearchUseCase(findUserPort);
  }
}
