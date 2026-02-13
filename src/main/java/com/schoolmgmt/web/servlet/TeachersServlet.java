package com.schoolmgmt.web.servlet;

import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TeachersServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("teachers", teacherService(req).listTeachers());
        req.setAttribute("activePage", "teachers");
        req.getRequestDispatcher("/WEB-INF/views/teachers.jsp").forward(req, resp);
    }
}
