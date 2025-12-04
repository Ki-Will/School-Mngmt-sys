package com.schoolmgmt.service;

import com.schoolmgmt.model.Course;

import java.util.Collection;
import java.util.Optional;

public interface CourseService {
    void addCourse(Course course);

    Optional<Course> findCourse(String code);

    Collection<Course> listCourses();

    void assignTeacher(String courseCode, String teacherId);
}
