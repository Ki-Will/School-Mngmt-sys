package com.schoolmgmt.service;

import com.schoolmgmt.model.UserAccount;

import java.util.Optional;

public interface UserAccountService {
    UserAccount register(String username, String password, String role);

    Optional<UserAccount> authenticate(String username, String password);

    Optional<UserAccount> findByUsername(String username);
}
