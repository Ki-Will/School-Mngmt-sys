package com.schoolmgmt.util;

import com.schoolmgmt.model.Student;

import java.util.Comparator;

public class StudentAverageComparator implements Comparator<Student> {
    @Override
    public int compare(Student left, Student right) {
        int averageComparison = Double.compare(right.calculateAverageGrade(), left.calculateAverageGrade());
        if (averageComparison != 0) {
            return averageComparison;
        }
        return left.getId().compareTo(right.getId());
    }
}
