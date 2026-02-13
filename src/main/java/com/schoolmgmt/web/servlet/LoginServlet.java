package com.schoolmgmt.web.servlet;

import com.schoolmgmt.model.UserAccount;
import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class LoginServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (currentUser(req).isPresent()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.setAttribute("activePage", "login");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Optional<UserAccount> authenticated = userAccountService(req).authenticate(username, password);
        if (authenticated.isPresent()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", authenticated.get());
            session.setAttribute("isAdmin", "ADMIN".equals(authenticated.get().getRole()));
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("error", "Invalid username or password");
            req.setAttribute("activePage", "login");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
