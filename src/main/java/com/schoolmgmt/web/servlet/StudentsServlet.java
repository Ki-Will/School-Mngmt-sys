package com.schoolmgmt.web.servlet;

import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StudentsServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("students", studentService(req).listStudents());
        req.setAttribute("activePage", "students");
        req.getRequestDispatcher("/WEB-INF/views/students.jsp").forward(req, resp);
    }
}
