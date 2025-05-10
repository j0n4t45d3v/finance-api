package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.User;
import br.com.managementfinanceapi.application.port.in.user.ChangePassword;
import br.com.managementfinanceapi.application.port.in.user.FindOneUser;
import br.com.managementfinanceapi.adapter.out.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordUseCase implements ChangePassword {

  private final FindOneUser findOneUser;
  private final UserRepository userRepository;

  public ChangePasswordUseCase(FindOneUser findOneUser, UserRepository userRepository) {
    this.findOneUser = findOneUser;
    this.userRepository = userRepository;
  }

  @Override
  public void change(String email, String password) {
    User user = this.findOneUser.byEmail(email);
    user.changePassword(password);
    this.userRepository.save(user);
  }
}
