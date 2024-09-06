package com.management.finance.user;

import com.management.finance.user.dto.RegisterUser;
import com.management.finance.user.dto.ReturnUser;
import com.management.finance.user.dto.UpdateUser;

import java.util.List;

public interface UserService {
    ReturnUser register(RegisterUser user);
    List<ReturnUser> getAll();
    ReturnUser getById(Long id);
    ReturnUser getByEmail(String email);
    void deleteById(Long id);
    void update(UpdateUser userUpdated,Long id);
}
