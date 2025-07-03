package br.com.managementfinanceapi.infra.bean.user;

import br.com.managementfinanceapi.adapter.out.repository.user.UserRepository;
import br.com.managementfinanceapi.application.core.usecase.user.ChangePasswordUseCase;
import br.com.managementfinanceapi.application.core.usecase.user.GetUserDetailsUseCase;
import br.com.managementfinanceapi.application.core.usecase.user.LoginUseCase;
import br.com.managementfinanceapi.application.core.usecase.user.RegisterUserUseCase;
import br.com.managementfinanceapi.application.core.usecase.user.SearchUseCase;
import br.com.managementfinanceapi.application.port.in.user.ChangePasswordPort;
import br.com.managementfinanceapi.application.port.in.user.LoginPort;
import br.com.managementfinanceapi.application.port.in.user.RegisterUserPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserBean {

  @Bean
  public RegisterUserPort registerUser(
    SaveUserPort saveUserPort, 
    FindUserPort findUserPort,
    HashPasswordPort hashPasswordPort
  ) {
    return new RegisterUserUseCase(saveUserPort, findUserPort, hashPasswordPort);
  }

  @Bean
  public SearchUserPort searchUser(FindUserPort findUserPort) {
    return new SearchUseCase(findUserPort);
  }

  @Bean
  public LoginPort login(SearchUserPort searchUserPort, HashPasswordPort hashPasswordPort) {
    return new LoginUseCase(searchUserPort, hashPasswordPort);
  }

  @Bean
  public ChangePasswordPort changePassword(
      SearchUserPort searchUserPort,
      HashPasswordPort hashPasswordPort,
      SaveUserPort saveUserPort) {
    return new ChangePasswordUseCase(searchUserPort, hashPasswordPort, saveUserPort);
  }

  @Bean
  public UserDetailsService getUserDetails(
      UserRepository userRepository) {
    return new GetUserDetailsUseCase(userRepository);
  }

}