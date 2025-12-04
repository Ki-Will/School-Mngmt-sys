package com.schoolmgmt.service.impl;

import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.model.Course;
import com.schoolmgmt.model.Enrollment;
import com.schoolmgmt.model.GradeRecord;
import com.schoolmgmt.model.Student;
import com.schoolmgmt.repository.InMemoryRepository;
import com.schoolmgmt.service.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {
    private final SchoolRecords records;

    public StudentServiceImpl(SchoolRecords records) {
        this.records = records;
    }

    @Override
    public void registerStudent(Student student) {
        InMemoryRepository<String, Student> repository = records.getStudents();
        if (repository.existsById(student.getId())) {
            throw new IllegalArgumentException("Student with id " + student.getId() + " already exists");
        }
        repository.save(student);
    }

    @Override
    public Optional<Student> findStudentById(String studentId) {
        return records.getStudents().findById(studentId);
    }

    @Override
    public Collection<Student> listStudents() {
        return new ArrayList<>(records.getStudents().findAll());
    }

    @Override
    public void enrollStudent(String studentId, String courseCode) {
        Student student = records.getStudents()
                .findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown student: " + studentId));

        Course course = records.getCourses()
                .findById(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown course: " + courseCode));

        String enrollmentId = studentId + "::" + courseCode;
        if (records.getEnrollments().existsById(enrollmentId)) {
            throw new IllegalStateException("Student already enrolled in course");
        }

        Enrollment enrollment = new Enrollment(student.getId(), course.getCode(), LocalDate.now());
        student.addEnrollment(enrollment);
        records.getEnrollments().save(enrollment);
    }

    @Override
    public Collection<Enrollment> listEnrollmentsForStudent(String studentId) {
        return records.getEnrollments()
                .findAll()
                .stream()
                .filter(enrollment -> enrollment.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public void recordGrade(String studentId, String courseCode, GradeRecord gradeRecord) {
        String enrollmentId = studentId + "::" + courseCode;
        Enrollment enrollment = records.getEnrollments()
                .findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("No enrollment for student " + studentId + " in course " + courseCode));

        enrollment.setGradeRecord(gradeRecord);
    }
}
