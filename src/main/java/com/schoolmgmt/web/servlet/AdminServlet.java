package com.schoolmgmt.web.servlet;

import com.schoolmgmt.model.*;
import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("activePage", "admin");
        List<String> studentIds = studentService(req).findAllStudents().stream().map(Student::getId).collect(Collectors.toList());
        req.setAttribute("studentIds", studentIds);
        List<String> teacherIds = teacherService(req).findAllTeachers().stream().map(Teacher::getId).collect(Collectors.toList());
        req.setAttribute("teacherIds", teacherIds);
        List<String> courseCodes = courseService(req).findAllCourses().stream().map(Course::getCode).collect(Collectors.toList());
        req.setAttribute("courseCodes", courseCodes);
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }
}
