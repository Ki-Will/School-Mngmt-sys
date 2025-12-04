package com.schoolmgmt.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class GradeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String assignmentName;
    private final double score;
    private final LocalDate recordedDate;

    public GradeRecord(String assignmentName, double score, LocalDate recordedDate) {
        this.assignmentName = Objects.requireNonNull(assignmentName, "assignmentName");
        this.score = score;
        this.recordedDate = Objects.requireNonNull(recordedDate, "recordedDate");
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public double getScore() {
        return score;
    }

    public LocalDate getRecordedDate() {
        return recordedDate;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f on %s", assignmentName, score, recordedDate);
    }
}
