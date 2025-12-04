package com.schoolmgmt.data;

import com.schoolmgmt.model.Course;
import com.schoolmgmt.model.Enrollment;
import com.schoolmgmt.model.Staff;
import com.schoolmgmt.model.Student;
import com.schoolmgmt.model.Teacher;
import com.schoolmgmt.repository.InMemoryRepository;

import java.io.Serializable;

public class SchoolRecords implements Serializable {
    private static final long serialVersionUID = 1L;

    private final InMemoryRepository<String, Student> students = new InMemoryRepository<>();
    private final InMemoryRepository<String, Teacher> teachers = new InMemoryRepository<>();
    private final InMemoryRepository<String, Course> courses = new InMemoryRepository<>();
    private final InMemoryRepository<String, Enrollment> enrollments = new InMemoryRepository<>();
    private final InMemoryRepository<String, Staff> staff = new InMemoryRepository<>();

    public InMemoryRepository<String, Student> getStudents() {
        return students;
    }

    public InMemoryRepository<String, Teacher> getTeachers() {
        return teachers;
    }

    public InMemoryRepository<String, Course> getCourses() {
        return courses;
    }

    public InMemoryRepository<String, Enrollment> getEnrollments() {
        return enrollments;
    }

    public InMemoryRepository<String, Staff> getStaff() {
        return staff;
    }
}
