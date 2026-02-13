package com.schoolmgmt.web.servlet;

import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StaffServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("staff", staffService(req).listStaff());
        req.setAttribute("activePage", "staff");
        req.getRequestDispatcher("/WEB-INF/views/staff.jsp").forward(req, resp);
    }
}
