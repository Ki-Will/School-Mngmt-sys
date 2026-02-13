package com.schoolmgmt.service.impl;

import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.model.UserAccount;
import com.schoolmgmt.repository.InMemoryRepository;
import com.schoolmgmt.service.UserAccountService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

public class UserAccountServiceImpl implements UserAccountService {
    private final InMemoryRepository<String, UserAccount> repository;

    public UserAccountServiceImpl(SchoolRecords records) {
        this.repository = records.getUserAccounts();
    }

    @Override
    public UserAccount register(String username, String password, String role) {
        repository.findAll().stream()
                .filter(account -> account.getUsername().equalsIgnoreCase(username))
                .findAny()
                .ifPresent(account -> {
                    throw new IllegalArgumentException("Username already exists");
                });

        String id = UUID.randomUUID().toString();
        String hash = hashPassword(password);
        UserAccount account = new UserAccount(id, username, hash, role);
        repository.save(account);
        return account;
    }

    @Override
    public Optional<UserAccount> authenticate(String username, String password) {
        String hash = hashPassword(password);
        return repository.findAll().stream()
                .filter(account -> account.getUsername().equalsIgnoreCase(username))
                .filter(account -> account.getPasswordHash().equals(hash))
                .findFirst();
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return repository.findAll().stream()
                .filter(account -> account.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
