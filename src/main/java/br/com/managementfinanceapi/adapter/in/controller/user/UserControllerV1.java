package br.com.managementfinanceapi.adapter.in.controller.user;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.user.EditPassword;
import br.com.managementfinanceapi.adapter.in.dto.user.UserResponse;
import br.com.managementfinanceapi.application.port.in.user.ChangePasswordPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserControllerV1 {

  private final SearchUserPort searchUserPort;
  private final ChangePasswordPort editPassword;

  public UserControllerV1(SearchUserPort searchUserPort, ChangePasswordPort editPassword) {
    this.searchUserPort = searchUserPort;
    this.editPassword = editPassword;
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<ResponseV0<UserResponse>> findById(@PathVariable("id") Long id) {
    var resultFounded = this.searchUserPort.byId(id);
    var parseUsertoUserResponse = UserResponse.from(resultFounded);
    var response = ResponseV0.ok(parseUsertoUserResponse);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<ResponseV0<UserResponse>> findById(@PathVariable("email") String email) {
    var resultFounded = this.searchUserPort.byEmail(email);
    var parseUsertoUserResponse = UserResponse.from(resultFounded);
    var response = ResponseV0.ok(parseUsertoUserResponse);
    return ResponseEntity.ok(response);
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