package com.schoolmgmt.model;

import java.time.Duration;
import java.util.Objects;

public class Course implements Comparable<Course>, Identifiable<String> {
    private static final long serialVersionUID = 1L;

    private final String code;
    private String title;
    private Duration weeklyDuration;
    private String teacherId;

    public Course(String code, String title, Duration weeklyDuration) {
        this.code = Objects.requireNonNull(code, "code");
        this.title = Objects.requireNonNull(title, "title");
        this.weeklyDuration = Objects.requireNonNull(weeklyDuration, "weeklyDuration");
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getId() {
        return getCode();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "title");
    }

    public Duration getWeeklyDuration() {
        return weeklyDuration;
    }

    public void setWeeklyDuration(Duration weeklyDuration) {
        this.weeklyDuration = Objects.requireNonNull(weeklyDuration, "weeklyDuration");
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void assignTeacher(String teacherId) {
        this.teacherId = Objects.requireNonNull(teacherId, "teacherId");
    }

    @Override
    public int compareTo(Course other) {
        int titleComparison = this.title.compareToIgnoreCase(other.title);
        if (titleComparison != 0) {
            return titleComparison;
        }
        return this.code.compareTo(other.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return code.equals(course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("Course[%s - %s, weekly %s hours]", code, title, weeklyDuration.toHours());
    }
}
