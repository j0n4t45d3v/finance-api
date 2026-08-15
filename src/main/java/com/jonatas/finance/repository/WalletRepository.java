package com.jonatas.finance.repository;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.domain.Wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    boolean existsByDescriptionAndUser(Wallet.Description description, User user);
    boolean existsByDescriptionAndUserNotAndId(Wallet.Description description, User user, Long walletId);

    @Query("""
               select case
                          when count(a) > 0 then true
                          else false
                      end
               from Wallet a
               where a.user = :user
                 and a.main = true
           """)
    boolean existsMainWalletForUser(@Param("user") User user);

    @Query("""
               select case
                          when count(a) > 0 then true
                          else false
                      end
               from Wallet a
               where a.user = :user
                 and a.main = true
                 and a.id <> :walletId
           """)
    boolean existsMainWalletForUser(@Param("user") User user, Long walletId);

    List<Wallet> findAllByUser(User user);

    Optional<Wallet> findByIdAndUser(Long id, User user);
}
