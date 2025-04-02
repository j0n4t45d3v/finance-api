package br.com.managementfinanceapi.user.usecases;

import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.gateways.ChangePassword;
import br.com.managementfinanceapi.user.gateways.FindOneUser;
import br.com.managementfinanceapi.user.repository.UserRepository;
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
