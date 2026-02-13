package com.schoolmgmt.service;

import com.schoolmgmt.model.Course;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    void addCourse(Course course);

    Optional<Course> findCourse(String code);

    Collection<Course> listCourses();

    List<Course> findAllCourses();

    void assignTeacher(String courseCode, String teacherId);
}
