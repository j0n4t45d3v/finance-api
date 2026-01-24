package com.jonatas.finance.service.impl;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.exception.UnsuccessfulCreateUserException;
import com.jonatas.finance.repository.UserRepository;
import com.jonatas.finance.service.CreateUserService;
import org.springframework.stereotype.Service;

@Service
public class CreateUserServiceImpl implements CreateUserService {

    private final UserRepository userRepository;

    public CreateUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(User user) {
        this.userRepository.findByEmail(user.getEmail())
                .ifPresent(userFound -> {throw new UnsuccessfulCreateUserException();});
        return this.userRepository.save(user);
    }

}
