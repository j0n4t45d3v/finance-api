package br.com.managementfinanceapi.adapter.out.repository.user;

import br.com.managementfinanceapi.adapter.in.entity.user.UserEntity;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import br.com.managementfinanceapi.adapter.common.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class SaveUsePortImpl implements SaveUserPort {

  private final UserRepository repository;
  private final Mapper<UserEntity, UserDomain> mapper;

  public SaveUsePortImpl(UserRepository repository, Mapper<UserEntity, UserDomain> mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public UserDomain execute(UserDomain user) {
    UserEntity save = this.repository.save(this.mapper.toEntity(user));
    return this.mapper.toDomain(save);
  }

}
