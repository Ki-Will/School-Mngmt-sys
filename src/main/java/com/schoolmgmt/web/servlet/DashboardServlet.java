package com.schoolmgmt.web.servlet;

import com.schoolmgmt.model.Course;
import com.schoolmgmt.model.Enrollment;
import com.schoolmgmt.model.Staff;
import com.schoolmgmt.model.Student;
import com.schoolmgmt.model.Teacher;
import com.schoolmgmt.web.BaseServlet;
import com.schoolmgmt.web.view.RecentEnrollmentView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardServlet extends BaseServlet {
    private static final int MAX_RECENT = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("activePage", "dashboard");

        List<Student> students = studentService(req).listStudents().stream().collect(Collectors.toList());
        List<Teacher> teachers = teacherService(req).listTeachers().stream().collect(Collectors.toList());
        List<Course> courses = courseService(req).listCourses().stream().collect(Collectors.toList());
        List<Staff> staff = staffService(req).listStaff().stream().collect(Collectors.toList());

        req.setAttribute("studentCount", students.size());
        req.setAttribute("teacherCount", teachers.size());
        req.setAttribute("courseCount", courses.size());
        req.setAttribute("staffCount", staff.size());
        req.setAttribute("currentUser", currentUser(req).orElse(null));

        Map<String, String> studentNames = students.stream()
                .collect(Collectors.toMap(Student::getId, Student::getFullName));
        Map<String, String> courseTitles = courses.stream()
                .collect(Collectors.toMap(Course::getCode, Course::getTitle));

        List<RecentEnrollmentView> recentEnrollments = records(req).getEnrollments().findAll().stream()
                .sorted(Comparator.comparing(Enrollment::getEnrollmentDate,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(MAX_RECENT)
                .map(enrollment -> new RecentEnrollmentView(
                        studentNames.getOrDefault(enrollment.getStudentId(), "Unknown student"),
                        courseTitles.getOrDefault(enrollment.getCourseCode(), "Unknown course"),
                        enrollment.getEnrollmentDate()
                ))
                .collect(Collectors.toList());

        req.setAttribute("recentEnrollments", recentEnrollments);
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
