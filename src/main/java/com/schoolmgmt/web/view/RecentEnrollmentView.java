package com.schoolmgmt.web.view;

import java.time.LocalDate;
import java.util.Objects;

public class RecentEnrollmentView {
    private final String studentName;
    private final String courseTitle;
    private final LocalDate enrollmentDate;

    public RecentEnrollmentView(String studentName, String courseTitle, LocalDate enrollmentDate) {
        this.studentName = Objects.requireNonNullElse(studentName, "Unknown student");
        this.courseTitle = Objects.requireNonNullElse(courseTitle, "Unknown course");
        this.enrollmentDate = enrollmentDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
}
