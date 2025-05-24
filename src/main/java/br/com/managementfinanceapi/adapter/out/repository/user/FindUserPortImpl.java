package br.com.managementfinanceapi.adapter.out.repository.user;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindUserPortImpl implements FindUserPort {

  private final UserRepository repository;
  private final Mapper<UserEntity, UserDomain> mapper;

  public FindUserPortImpl(UserRepository repository, Mapper<UserEntity, UserDomain> mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<UserDomain> byEmail(String email) {
    Optional<UserEntity> userFound = this.repository.findByEmail(email);
    return userFound.map(this.mapper::toDomain);
  }

  @Override
  public Optional<UserDomain> byId(Long id) {
    return Optional.empty();
  }
}