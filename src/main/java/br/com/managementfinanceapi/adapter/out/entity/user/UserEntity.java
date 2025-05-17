package br.com.managementfinanceapi.adapter.out.entity.user;

import br.com.managementfinanceapi.adapter.out.entity.TimestampEntity;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends TimestampEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String email;
  private String password;

  public UserEntity(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public UserEntity(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public UserEntity() {}

  public UserEntity(Long userId){
    this.id = userId;
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
    return password;
  }

  public boolean differentPassword(String confirmPassword) {
    return !this.password.matches(confirmPassword);
  }

  public void changePassword(String password) {
    this.password = password;
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