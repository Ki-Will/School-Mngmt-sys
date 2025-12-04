package com.schoolmgmt.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Teacher extends Person {
    private static final long serialVersionUID = 1L;

    private final LocalDate hireDate;
    private final Set<String> expertise = new HashSet<>();

    public Teacher(String id, String fullName, String email, String phoneNumber, LocalDate hireDate) {
        super(id, fullName, email, phoneNumber);
        this.hireDate = Objects.requireNonNull(hireDate, "hireDate");
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public Set<String> getExpertise() {
        return Collections.unmodifiableSet(expertise);
    }

    public void addExpertise(String subject) {
        expertise.add(Objects.requireNonNull(subject, "subject"));
    }

    @Override
    public String getRole() {
        return "Teacher";
    }
}
