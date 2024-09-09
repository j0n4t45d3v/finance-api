package com.management.finance.user.impl;

import com.management.finance.user.User;
import com.management.finance.user.UserRepository;
import com.management.finance.user.UserService;
import com.management.finance.user.dto.RegisterUser;
import com.management.finance.user.dto.ReturnUser;
import com.management.finance.user.dto.UpdateUser;
import com.management.finance.util.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String NOT_FOUND_MESSAGE = "User not found";
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReturnUser register(RegisterUser user) {
        if(!user.confirmPasswordEqualsPassword()) {
            throw new RuntimeException("Confirm password is not equal password");
        }
        boolean userAlreadyExists = this.userRepository.existsByEmail(user.email());
        if(userAlreadyExists) {
            throw new RuntimeException("User already exists");
        }
        User userMapped = this.mapToEntity(user);
        //TODO: implement encryp password
        User userSaved = this.userRepository.save(userMapped);
        return ReturnUser.of(userSaved);
    }

    @Override
    public List<ReturnUser> getAll() {
        return this.userRepository
                .findAll().stream()
                .map(ReturnUser::of)
                .toList();
    }

    @Override
    public ReturnUser getById(Long id) {
        return this.userRepository.findById(id)
                .map(ReturnUser::of)
                .orElseThrow(
                        () -> new RuntimeException(NOT_FOUND_MESSAGE)
                );
    }

    @Override
    public ReturnUser getByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .map(ReturnUser::of)
                .orElseThrow(
                        () -> new RuntimeException(NOT_FOUND_MESSAGE)
                );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.userNotExist(id);
        this.userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(UpdateUser userUpdated, Long id) {
        User userFound = this.userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(NOT_FOUND_MESSAGE)
                );
        userFound.setEmail(userUpdated.email());
        userFound.setPassword(userUpdated.password());
        this.userRepository.save(userFound);
    }


    private void userNotExist(Long id) {
        boolean userExists = this.userRepository.existsById(id);
        if(!userExists) {
            throw new RuntimeException(NOT_FOUND_MESSAGE);
        }
    }

    private User mapToEntity(Record user) {
        Optional<User> dtoToEntity = Mapper.recordToEntity(user, User.class);
        if(dtoToEntity.isEmpty()){
            throw new RuntimeException("Failed in register user");
        }
        return dtoToEntity.get();
    }
}
