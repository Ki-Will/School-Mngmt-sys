package com.schoolmgmt.service.impl;

import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.model.Course;
import com.schoolmgmt.model.Teacher;
import com.schoolmgmt.service.CourseService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CourseServiceImpl implements CourseService {
    private final SchoolRecords records;

    public CourseServiceImpl(SchoolRecords records) {
        this.records = records;
    }

    @Override
    public void addCourse(Course course) {
        if (records.getCourses().existsById(course.getId())) {
            throw new IllegalArgumentException("Course already exists: " + course.getCode());
        }
        records.getCourses().save(course);
    }

    @Override
    public Optional<Course> findCourse(String code) {
        return records.getCourses().findById(code);
    }

    @Override
    public Collection<Course> listCourses() {
        return new ArrayList<>(records.getCourses().findAll());
    }

    @Override
    public List<Course> findAllCourses() {
        return new ArrayList<>(records.getCourses().findAll());
    }

    @Override
    public void assignTeacher(String courseCode, String teacherId) {
        Course course = records.getCourses()
                .findById(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown course: " + courseCode));

        Teacher teacher = records.getTeachers()
                .findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown teacher: " + teacherId));

        course.assignTeacher(teacher.getId());
    }
}
