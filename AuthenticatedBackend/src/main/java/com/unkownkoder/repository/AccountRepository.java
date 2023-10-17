package com.unkownkoder.repository;

import com.unkownkoder.models.Account;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByUser(User user);
}
