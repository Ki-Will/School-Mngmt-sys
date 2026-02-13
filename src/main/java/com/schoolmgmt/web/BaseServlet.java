package com.schoolmgmt.web;

import com.schoolmgmt.config.ApplicationContext;
import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.model.UserAccount;
import com.schoolmgmt.service.CourseService;
import com.schoolmgmt.service.StaffService;
import com.schoolmgmt.service.StudentService;
import com.schoolmgmt.service.TeacherService;
import com.schoolmgmt.service.UserAccountService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public abstract class BaseServlet extends HttpServlet {
    protected ApplicationContext applicationContext(HttpServletRequest request) {
        return (ApplicationContext) request.getServletContext().getAttribute(AppContextListener.CONTEXT_ATTRIBUTE);
    }

    protected StudentService studentService(HttpServletRequest request) {
        return applicationContext(request).getStudentService();
    }

    protected TeacherService teacherService(HttpServletRequest request) {
        return applicationContext(request).getTeacherService();
    }

    protected CourseService courseService(HttpServletRequest request) {
        return applicationContext(request).getCourseService();
    }

    protected StaffService staffService(HttpServletRequest request) {
        return applicationContext(request).getStaffService();
    }

    protected UserAccountService userAccountService(HttpServletRequest request) {
        return applicationContext(request).getUserAccountService();
    }

    protected SchoolRecords records(HttpServletRequest request) {
        return applicationContext(request).getRecords();
    }

    protected Optional<UserAccount> currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Optional.empty();
        }
        Object attribute = session.getAttribute("currentUser");
        if (attribute instanceof UserAccount userAccount) {
            return Optional.of(userAccount);
        }
        return Optional.empty();
    }
}
