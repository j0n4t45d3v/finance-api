package com.jonatas.finance.repository;

import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.Transaction;
import com.jonatas.finance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByDescriptionAndUser(Account.Description description, User user);

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

    List<Account> findAllByUser(User user);

}
