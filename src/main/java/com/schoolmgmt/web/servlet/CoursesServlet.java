package com.schoolmgmt.web.servlet;

import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CoursesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("courses", courseService(req).listCourses());
        req.setAttribute("activePage", "courses");
        req.getRequestDispatcher("/WEB-INF/views/courses.jsp").forward(req, resp);
    }
}
