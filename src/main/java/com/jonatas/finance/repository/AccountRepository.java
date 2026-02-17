package com.jonatas.finance.repository;

import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByDescriptionAndUser(Account.Description description, User user);
    boolean existsByDescriptionAndUserNotAndId(Account.Description description, User user, Long accountId);

    @Query("""
               select case
                          when count(a) > 0 then true
                          else false
                      end
               from Account a
               where a.user = :user
                 and a.main = true
           """)
    boolean existsMainAccountForUser(@Param("user") User user);

    @Query("""
               select case
                          when count(a) > 0 then true
                          else false
                      end
               from Account a
               where a.user = :user
                 and a.main = true
                 and a.id <> :accountId
           """)
    boolean existsMainAccountForUser(@Param("user") User user, Long accountId);

    List<Account> findAllByUser(User user);

    Optional<Account> findByIdAndUser(Long id, User user);
}
