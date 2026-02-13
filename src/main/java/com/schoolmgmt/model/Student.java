package com.schoolmgmt.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Student extends Person {
    private static final long serialVersionUID = 1L;

    private final LocalDate enrollmentDate;
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Student(String id, String fullName, String email, String phoneNumber, LocalDate enrollmentDate) {
        super(id, fullName, email, phoneNumber);
        this.enrollmentDate = Objects.requireNonNull(enrollmentDate, "enrollmentDate");
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(Objects.requireNonNull(enrollment, "enrollment"));
    }

    public double calculateAverageGrade() {
        return enrollments.stream()
                .map(Enrollment::getGradeRecord)
                .filter(Objects::nonNull)
                .mapToDouble(GradeRecord::getScore)
                .average()
                .orElse(0.0);
    }

    @Override
    public String getRole() {
        return "Student";
    }
}
