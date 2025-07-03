package br.com.managementfinanceapi.adapter.out.mapper.user;

import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

  @InjectMocks
  private UserMapper mapper;

  @Test
  @DisplayName("should parse user domain object to user entity")
  void shouldParseUserDomainObjectToUserEntity() {
    UserDomain domain = new UserDomain(1L, "test@test.com", Password.fromEncoded("test encoded"));
    UserEntity entity = this.mapper.toEntity(domain);
    assertEquals(domain.getId(), entity.getId());
    assertEquals(domain.getEmail(), entity.getEmail());
    assertEquals(domain.getPassword(), entity.getPassword());
  }

  @Test
  @DisplayName("should parse user entity object to user domain")
  void shouldParseUserEntityObjectToUserDomain() {
    UserEntity entity = new UserEntity(1L, "test@test.com", "test encoded");
    UserDomain domain = this.mapper.toDomain(entity);
    assertEquals(entity.getId(), domain.getId());
    assertEquals(entity.getEmail(), domain.getEmail());
    assertEquals(entity.getPassword(), domain.getPassword());
  }
}
