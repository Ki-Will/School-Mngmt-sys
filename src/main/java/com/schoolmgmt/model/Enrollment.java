package com.schoolmgmt.model;

import java.time.LocalDate;
import java.util.Objects;

public class Enrollment implements Identifiable<String> {
    private static final long serialVersionUID = 1L;

    private final String studentId;
    private final String courseCode;
    private final LocalDate enrollmentDate;
    private GradeRecord gradeRecord;

    public Enrollment(String studentId, String courseCode, LocalDate enrollmentDate) {
        this.studentId = Objects.requireNonNull(studentId, "studentId");
        this.courseCode = Objects.requireNonNull(courseCode, "courseCode");
        this.enrollmentDate = Objects.requireNonNull(enrollmentDate, "enrollmentDate");
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public String getId() {
        return studentId + "::" + courseCode;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public GradeRecord getGradeRecord() {
        return gradeRecord;
    }

    public void setGradeRecord(GradeRecord gradeRecord) {
        this.gradeRecord = gradeRecord;
    }

    @Override
    public String toString() {
        return String.format("Enrollment[student=%s, course=%s, date=%s, grade=%s]",
                studentId, courseCode, enrollmentDate, gradeRecord == null ? "N/A" : gradeRecord);
    }
}
