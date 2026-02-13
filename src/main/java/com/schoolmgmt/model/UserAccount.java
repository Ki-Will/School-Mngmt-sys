package com.schoolmgmt.model;

import java.util.Objects;

public class UserAccount implements Identifiable<String> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String username;
    private final String passwordHash;
    private final String role;

    public UserAccount(String id, String username, String passwordHash, String role) {
        this.id = Objects.requireNonNull(id, "id");
        this.username = Objects.requireNonNull(username, "username");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash");
        this.role = Objects.requireNonNull(role, "role");
    }

    @Override
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }
}
