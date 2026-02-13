package com.schoolmgmt.service.impl;

import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.model.Teacher;
import com.schoolmgmt.repository.InMemoryRepository;
import com.schoolmgmt.service.TeacherService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TeacherServiceImpl implements TeacherService {
    private final SchoolRecords records;

    public TeacherServiceImpl(SchoolRecords records) {
        this.records = records;
    }

    @Override
    public void hireTeacher(Teacher teacher) {
        InMemoryRepository<String, Teacher> repository = records.getTeachers();
        if (repository.existsById(teacher.getId())) {
            throw new IllegalArgumentException("Teacher already exists: " + teacher.getId());
        }
        repository.save(teacher);
    }

    @Override
    public Optional<Teacher> findTeacher(String teacherId) {
        return records.getTeachers().findById(teacherId);
    }

    @Override
    public Collection<Teacher> listTeachers() {
        return new ArrayList<>(records.getTeachers().findAll());
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return new ArrayList<>(records.getTeachers().findAll());
    }
}
