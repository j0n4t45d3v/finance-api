package com.jonatas.finance.service.impl;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.exception.UnsuccessfulCreateUserException;
import com.jonatas.finance.repository.UserRepository;
import com.jonatas.finance.service.CreateUserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserServiceImpl implements CreateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserServiceImpl(
          UserRepository userRepository,
          PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(User user) {
        this.userRepository.findByEmail(user.getEmail())
                .ifPresent(userFound -> {throw new UnsuccessfulCreateUserException();});
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

}
