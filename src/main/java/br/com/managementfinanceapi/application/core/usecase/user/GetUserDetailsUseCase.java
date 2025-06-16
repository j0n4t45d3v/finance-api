package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.repository.user.UserRepository;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class GetUserDetailsUseCase implements UserDetailsService {

  private final UserRepository userRepository;

  public GetUserDetailsUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserEntity> userFound = this.userRepository.findByEmail(username);
    if(userFound.isEmpty()) throw new UsernameNotFoundException("Usuário não existe!");
    return userFound.get();
  }
}