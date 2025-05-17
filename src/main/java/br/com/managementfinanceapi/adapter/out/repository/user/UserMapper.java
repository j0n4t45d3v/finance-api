package br.com.managementfinanceapi.adapter.out.repository.user;

import br.com.managementfinanceapi.adapter.in.entity.user.UserEntity;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import br.com.managementfinanceapi.adapter.common.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserEntity, UserDomain> {

  @Override
  public UserEntity toEntity(UserDomain domain) {
    return new UserEntity(domain.getId(), domain.getEmail(), domain.getPassword());
  }

  @Override
  public UserDomain toDomain(UserEntity entity) {
    return new UserDomain(entity.getId(), entity.getEmail(), Password.from(entity.getPassword()));
  }
}
