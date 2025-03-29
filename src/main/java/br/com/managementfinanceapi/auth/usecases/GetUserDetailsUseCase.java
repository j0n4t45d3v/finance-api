package br.com.managementfinanceapi.auth.usecases;

import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.repository.UserRepository;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetUserDetailsUseCase implements UserDetailsService {

  private final UserRepository userRepository;

  public GetUserDetailsUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userFound = this.userRepository.findByEmail(username);
    if(userFound.isEmpty()) throw new UsernameNotFoundException("Usuário não existe!");
    return userFound.get();
  }
}