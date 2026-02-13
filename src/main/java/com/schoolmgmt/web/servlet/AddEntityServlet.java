package com.schoolmgmt.web.servlet;

import com.schoolmgmt.model.UserAccount;
import com.schoolmgmt.model.Student;
import com.schoolmgmt.model.Staff;
import com.schoolmgmt.model.Teacher;
import com.schoolmgmt.model.Course;
import com.schoolmgmt.model.GradeRecord;
import com.schoolmgmt.util.ActivityLogger;
import com.schoolmgmt.util.IdGenerator;
import com.schoolmgmt.util.ValidationUtils;
import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

public class AddEntityServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redirect to dashboard or show form
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (currentUser(req).map(UserAccount::getRole).filter("ADMIN"::equals).isEmpty()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }

        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter required");
            return;
        }

        try {
            switch (action) {
                case "addStudent":
                    addStudent(req);
                    break;
                case "addTeacher":
                    addTeacher(req);
                    break;
                case "addCourse":
                    addCourse(req);
                    break;
                case "addStaff":
                    addStaff(req);
                    break;
                case "enrollStudent":
                    enrollStudent(req);
                    break;
                case "assignTeacher":
                    assignTeacher(req);
                    break;
                case "recordGrade":
                    recordGrade(req);
                    break;
                default:
                    req.setAttribute("error", "Unknown action: " + action);
                    break;
            }
            req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error processing request: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
        }
    }

    private void addStudent(HttpServletRequest req) {
        String id = IdGenerator.newId("STU");
        String fullName = req.getParameter("fullName");
        String email = ValidationUtils.requireValidEmail(req.getParameter("email"));
        String phone = ValidationUtils.requireValidPhone(req.getParameter("phone"));
        LocalDate enrollmentDate = LocalDate.parse(req.getParameter("enrollmentDate"));

        Student student = new Student(id, fullName, email, phone, enrollmentDate);
        studentService(req).registerStudent(student);
        req.setAttribute("message", "Student added with ID: " + id);
        currentUser(req).ifPresent(user -> ActivityLogger.logActivity("Added Student", user.getUsername(), "ID: " + id));
    }

    private void addTeacher(HttpServletRequest req) {
        String id = IdGenerator.newId("TEA");
        String fullName = req.getParameter("fullName");
        String email = ValidationUtils.requireValidEmail(req.getParameter("email"));
        String phone = ValidationUtils.requireValidPhone(req.getParameter("phone"));
        LocalDate hireDate = LocalDate.parse(req.getParameter("hireDate"));
        String expertiseStr = req.getParameter("expertise");

        Teacher teacher = new Teacher(id, fullName, email, phone, hireDate);
        if (expertiseStr != null && !expertiseStr.trim().isEmpty()) {
            String[] expertise = expertiseStr.split(",");
            for (String exp : expertise) {
                teacher.addExpertise(exp.trim());
            }
        }

        teacherService(req).hireTeacher(teacher);
        req.setAttribute("message", "Teacher added with ID: " + id);
        currentUser(req).ifPresent(user -> ActivityLogger.logActivity("Added Teacher", user.getUsername(), "ID: " + id));
    }

    private void addCourse(HttpServletRequest req) {
        String code = req.getParameter("code");
        String title = req.getParameter("title");
        int weeklyHours = Integer.parseInt(req.getParameter("weeklyHours"));
        String teacherId = req.getParameter("teacherId");

        Course course = new Course(code, title, Duration.ofHours(weeklyHours));
        if (teacherId != null && !teacherId.isEmpty()) {
            course.assignTeacher(teacherId);
        }

        courseService(req).addCourse(course);
        req.setAttribute("message", "Course added with code: " + code);
        currentUser(req).ifPresent(user -> ActivityLogger.logActivity("Added Course", user.getUsername(), "Code: " + code));
    }

    private void addStaff(HttpServletRequest req) {
        String id = IdGenerator.newId("STF");
        String fullName = req.getParameter("fullName");
        String email = ValidationUtils.requireValidEmail(req.getParameter("email"));
        String phone = ValidationUtils.requireValidPhone(req.getParameter("phone"));
        LocalDate employmentDate = LocalDate.parse(req.getParameter("employmentDate"));
        String department = req.getParameter("department");

        Staff staff = new Staff(id, fullName, email, phone, employmentDate, department);
        staffService(req).addStaffMember(staff);
        req.setAttribute("message", "Staff added with ID: " + id);
        currentUser(req).ifPresent(user -> ActivityLogger.logActivity("Added Staff", user.getUsername(), "ID: " + id));
    }

    private void enrollStudent(HttpServletRequest req) {
        String studentId = req.getParameter("studentId");
        String courseCode = req.getParameter("courseCode");

        studentService(req).enrollStudent(studentId, courseCode);
        req.setAttribute("message", "Student enrolled in course successfully");
        currentUser(req).ifPresent(user -> ActivityLogger.logActivity("Enrolled Student", user.getUsername(), "Student: " + studentId + " Course: " + courseCode));
    }

    private void assignTeacher(HttpServletRequest req) {
        String courseCode = req.getParameter("courseCode");
        String teacherId = req.getParameter("teacherId");

        courseService(req).assignTeacher(courseCode, teacherId);
        req.setAttribute("message", "Teacher assigned to course successfully");
        currentUser(req).ifPresent(user -> ActivityLogger.logActivity("Assigned Teacher", user.getUsername(), "Teacher: " + teacherId + " Course: " + courseCode));
    }

    private void recordGrade(HttpServletRequest req) {
        String studentId = req.getParameter("studentId");
        String courseCode = req.getParameter("courseCode");
        String assignmentName = req.getParameter("assignmentName");
        double score = Double.parseDouble(req.getParameter("score"));
        LocalDate date = LocalDate.parse(req.getParameter("date"));

        GradeRecord gradeRecord = new GradeRecord(assignmentName, score, date);
        studentService(req).recordGrade(studentId, courseCode, gradeRecord);
        req.setAttribute("message", "Grade recorded for student successfully");
    }
}
