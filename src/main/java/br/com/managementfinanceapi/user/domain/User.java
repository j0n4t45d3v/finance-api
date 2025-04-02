package br.com.managementfinanceapi.user.domain;

import br.com.managementfinanceapi.common.baseentity.TimestampEntity;
import br.com.managementfinanceapi.user.domain.dto.CreateUser;
import br.com.managementfinanceapi.user.domain.dto.UserResponse;
import br.com.managementfinanceapi.user.domain.dvo.Password;
import br.com.managementfinanceapi.user.exceptions.InvalidPassword;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends TimestampEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String email;
  @Embedded
  private Password password;

  public User(Long id, String email, Password password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public User(String email, Password password) {
    this.email = email;
    this.password = password;
  }

  public User(CreateUser createUser) {
    this(createUser.email(), Password.from(createUser.password()));
  }

  public User() {}

  public User(UserResponse userFound) {
    this(userFound.id(), userFound.email(), null);
  }

  public User(Long userId){
    this.id = userId;
  }

  public UserResponse toResponse() {
    return new UserResponse(this.id, this.email);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password.getValue();
  }

  public void validatePassword(String confirmPassword) {
    if (!this.password.matches(confirmPassword)) {
      throw new InvalidPassword("A senhas não são iguais");
    }
  }

  public void changePassword(String password) {
    this.password = Password.from(password);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getUsername() {
    return this.email;
  }

}