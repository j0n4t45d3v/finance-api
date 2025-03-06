package br.com.managementfinanceapi.user.controller;

import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.user.domain.dto.EditPassword;
import br.com.managementfinanceapi.user.domain.dto.UserResponse;
import br.com.managementfinanceapi.user.gateways.ChangePassword;
import br.com.managementfinanceapi.user.gateways.FindOneUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserControllerV1 {

  private final FindOneUser findOneUser;
  private final ChangePassword editPassword;

  public UserControllerV1(FindOneUser findOneUser, ChangePassword editPassword) {
    this.findOneUser = findOneUser;
    this.editPassword = editPassword;
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<ResponseV0<UserResponse>> findById(@PathVariable("id") Long id) {
    UserResponse resultFounded = this.findOneUser.byId(id);
    return ResponseEntity.ok(ResponseV0.ok(resultFounded));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<ResponseV0<UserResponse>> findById(@PathVariable("email") String email) {
    UserResponse resultFounded = this.findOneUser.byEmail(email);
    return ResponseEntity.ok(ResponseV0.ok(resultFounded));
  }

  @PutMapping("/change_password/{email}")
  public ResponseEntity<ResponseV0<String>> changePassword(
      @PathVariable("email") String email,
      @RequestBody EditPassword body
  ) {
    this.editPassword.change(email, body.password());
    return ResponseEntity.ok(ResponseV0.ok("Senha modificada com sucesso"));
  }
}
