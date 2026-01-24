package com.jonatas.finance.repository;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.dvo.user.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(Email email);
}
