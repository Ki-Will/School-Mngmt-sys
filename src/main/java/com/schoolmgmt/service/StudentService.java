package com.schoolmgmt.service;

import com.schoolmgmt.model.Enrollment;
import com.schoolmgmt.model.GradeRecord;
import com.schoolmgmt.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    void registerStudent(Student student);

    Optional<Student> findStudentById(String studentId);

    Collection<Student> listStudents();

    List<Student> findAllStudents();

    void enrollStudent(String studentId, String courseCode);

    Collection<Enrollment> listEnrollmentsForStudent(String studentId);

    void recordGrade(String studentId, String courseCode, GradeRecord gradeRecord);
}
