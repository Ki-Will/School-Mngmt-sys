package com.schoolmgmt.service;

import com.schoolmgmt.model.Teacher;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TeacherService {
    void hireTeacher(Teacher teacher);

    Optional<Teacher> findTeacher(String teacherId);

    Collection<Teacher> listTeachers();

    List<Teacher> findAllTeachers();
}
