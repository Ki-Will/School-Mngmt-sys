package com.schoolmgmt.model;

import java.time.LocalDate;
import java.util.Objects;

public class Staff extends Person {
    private static final long serialVersionUID = 1L;

    private final LocalDate employmentDate;
    private String department;

    public Staff(String id, String fullName, String email, String phoneNumber, LocalDate employmentDate, String department) {
        super(id, fullName, email, phoneNumber);
        this.employmentDate = Objects.requireNonNull(employmentDate, "employmentDate");
        this.department = Objects.requireNonNull(department, "department");
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = Objects.requireNonNull(department, "department");
    }

    @Override
    public String getRole() {
        return "Staff";
    }
}
