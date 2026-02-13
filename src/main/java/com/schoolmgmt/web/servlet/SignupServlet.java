package com.schoolmgmt.web.servlet;

import com.schoolmgmt.model.UserAccount;
import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class SignupServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("activePage", "signup");
        req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("activePage", "signup");
        String username = Optional.ofNullable(req.getParameter("username")).map(String::trim).orElse("");
        String password = Optional.ofNullable(req.getParameter("password")).orElse("");
        String confirmPassword = Optional.ofNullable(req.getParameter("confirmPassword")).orElse("");
        String role = Optional.ofNullable(req.getParameter("role")).orElse("");

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank() || role.isBlank()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
            return;
        }

        String normalizedRole = role.toUpperCase(Locale.ROOT);
        if (!normalizedRole.equals("ADMIN") && !normalizedRole.equals("STAFF")) {
            req.setAttribute("error", "Please select a valid role.");
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
            return;
        }

        try {
            UserAccount account = userAccountService(req).register(username, password, normalizedRole);
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", account);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
        }
    }
}
