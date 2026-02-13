<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>School Management – Admin Panel</title>
    <style>
        :root {
            color-scheme: light;
            --bg: #eef2ff;
            --bg-accent: radial-gradient(circle at 20% 20%, rgba(79, 70, 229, 0.15), transparent 55%),
            radial-gradient(circle at 80% 0%, rgba(14, 165, 233, 0.18), transparent 50%),
            linear-gradient(180deg, #f5f7ff 0%, #eef2ff 70%, #e0e7ff 100%);
            --surface: rgba(255, 255, 255, 0.92);
            --surface-strong: rgba(255, 255, 255, 0.98);
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --primary-muted: rgba(37, 99, 235, 0.12);
            --text: #0f172a;
            --muted: #64748b;
            --border: rgba(148, 163, 184, 0.25);
            --border-strong: rgba(148, 163, 184, 0.4);
            --shadow-soft: 0 18px 40px rgba(15, 23, 42, 0.12);
            --shadow-strong: 0 24px 60px rgba(15, 23, 42, 0.16);
            --alert: #fee2e2;
            --alert-text: #b91c1c;
            --success: #dcfce7;
            --success-text: #166534;
            --nav-height: 4.5rem;
            --transition: 180ms cubic-bezier(0.4, 0, 0.2, 1);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif;
            background: var(--bg-accent);
            color: var(--text);
            line-height: 1.6;
        }

        .site-header {
            background: var(--surface-strong);
            border-bottom: 1px solid var(--border);
            padding: 1.75rem clamp(1.5rem, 5vw, 3rem);
            backdrop-filter: blur(16px);
            -webkit-backdrop-filter: blur(16px);
        }

        .site-footer {
            border-top: 1px solid var(--border);
            text-align: center;
            font-size: 0.875rem;
            color: var(--muted);
            padding: 2rem 1.5rem 2.5rem;
            background: var(--surface-strong);
            backdrop-filter: blur(12px);
        }

        .subtitle {
            margin: 0.25rem 0 0;
            color: var(--muted);
        }

        .admin-main {
            max-width: min(1200px, 92vw);
            margin: calc(var(--nav-height) + 1.5rem) auto 3rem;
            padding: 0 clamp(1rem, 4vw, 2.5rem);
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
            gap: 2rem;
        }

        .card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: clamp(1.8rem, 4vw, 2.4rem);
            box-shadow: var(--shadow-soft);
            position: relative;
            overflow: hidden;
            transition: transform var(--transition), box-shadow var(--transition), border-color var(--transition);
        }

        .card::after {
            content: "";
            position: absolute;
            inset: 0;
            background: linear-gradient(135deg, rgba(37, 99, 235, 0.05), transparent 60%);
            opacity: 0;
            transition: opacity var(--transition);
            pointer-events: none;
        }

        .card:hover,
        .card:focus-within {
            transform: translateY(-4px);
            border-color: var(--border-strong);
            box-shadow: var(--shadow-strong);
        }

        .card:hover::after,
        .card:focus-within::after {
            opacity: 1;
        }

        h2 {
            margin-top: 0;
            font-size: 1.5rem;
        }

        label {
            font-weight: 600;
            letter-spacing: 0.01em;
        }

        .field {
            margin-bottom: 1.25rem;
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        input, button {
            padding: 0.75rem 1rem;
            border-radius: 12px;
            border: 1px solid var(--border);
            font-size: 1rem;
            background: rgba(255, 255, 255, 0.92);
            transition: border-color var(--transition), box-shadow var(--transition), background var(--transition);
        }

        input:focus,
        button:focus {
            outline: 3px solid rgba(37, 99, 235, 0.35);
            outline-offset: 2px;
        }

        .field input:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
            background: #fff;
        }

        .field input:hover {
            border-color: var(--border-strong);
        }

        .btn-primary {
            background: var(--primary);
            border: none;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            transition: transform var(--transition), background var(--transition), box-shadow var(--transition);
            box-shadow: 0 10px 24px rgba(37, 99, 235, 0.18);
        }

        .btn-primary:hover,
        .btn-primary:focus {
            background: var(--primary-hover);
            transform: translateY(-2px);
            box-shadow: 0 18px 35px rgba(37, 99, 235, 0.24);
        }

        .secondary-link {
            color: var(--primary);
            text-decoration: none;
            font-weight: 600;
        }

        .secondary-link:hover,
        .secondary-link:focus {
            text-decoration: underline;
        }

        .back-link {
            display: inline-block;
            margin-bottom: 2rem;
            font-weight: 600;
        }

        .message {
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 8px;
        }
        .success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        @media (max-width: 768px) {
            .admin-main {
                grid-template-columns: 1fr;
                margin-top: 1.5rem;
            }
        }
    </style>
</head>
<body>
<header class="site-header" role="banner">
    <div class="brand">
        <h1>School Management Portal</h1>
        <p class="subtitle">Admin Panel</p>
        <a class="secondary-link" href="${pageContext.request.contextPath}/dashboard">← Back to Dashboard</a>
    </div>
</header>
<main role="main" class="admin-main">
    <c:if test="${not empty message}"><div class="message success">${message}</div></c:if>
    <c:if test="${not empty error}"><div class="message error">${error}</div></c:if>

    <section class="card" aria-labelledby="add-student-heading">
        <h2 id="add-student-heading">Add Student</h2>
        <form method="post" action="${pageContext.request.contextPath}/add-entity" novalidate>
            <input type="hidden" name="action" value="addStudent">
            <div class="field">
                <label for="studentFullName">Full Name</label>
                <input id="studentFullName" name="fullName" type="text" required>
            </div>
            <div class="field">
                <label for="studentEmail">Email</label>
                <input id="studentEmail" name="email" type="email" required>
            </div>
            <div class="field">
                <label for="studentPhone">Phone</label>
                <input id="studentPhone" name="phone" type="tel" required>
            </div>
            <div class="field">
                <label for="studentEnrollmentDate">Enrollment Date (YYYY-MM-DD)</label>
                <input id="studentEnrollmentDate" name="enrollmentDate" type="date" required>
            </div>
            <button type="submit" class="btn-primary">Add Student</button>
        </form>
    </section>

    <section class="card" aria-labelledby="add-teacher-heading">
        <h2 id="add-teacher-heading">Add Teacher</h2>
        <form method="post" action="${pageContext.request.contextPath}/add-entity" novalidate>
            <input type="hidden" name="action" value="addTeacher">
            <div class="field">
                <label for="teacherFullName">Full Name</label>
                <input id="teacherFullName" name="fullName" type="text" required>
            </div>
            <div class="field">
                <label for="teacherEmail">Email</label>
                <input id="teacherEmail" name="email" type="email" required>
            </div>
            <div class="field">
                <label for="teacherPhone">Phone</label>
                <input id="teacherPhone" name="phone" type="tel" required>
            </div>
            <div class="field">
                <label for="teacherHireDate">Hire Date (YYYY-MM-DD)</label>
                <input id="teacherHireDate" name="hireDate" type="date" required>
            </div>
            <div class="field">
                <label for="teacherExpertise">Expertise (comma-separated)</label>
                <input id="teacherExpertise" name="expertise" type="text" placeholder="e.g. Math, Physics">
            </div>
            <button type="submit" class="btn-primary">Add Teacher</button>
        </form>
    </section>

    <section class="card" aria-labelledby="add-course-heading">
        <h2 id="add-course-heading">Add Course</h2>
        <form method="post" action="${pageContext.request.contextPath}/add-entity" novalidate>
            <input type="hidden" name="action" value="addCourse">
            <div class="field">
                <label for="courseCode">Code</label>
                <input id="courseCode" name="code" type="text" required>
            </div>
            <div class="field">
                <label for="courseTitle">Title</label>
                <input id="courseTitle" name="title" type="text" required>
            </div>
            <div class="field">
                <label for="courseHours">Weekly Hours</label>
                <input id="courseHours" name="weeklyHours" type="number" required>
            </div>
            <div class="field">
                <label for="courseTeacherId">Teacher ID (optional)</label>
                <input id="courseTeacherId" name="teacherId" type="text">
            </div>
            <button type="submit" class="btn-primary">Add Course</button>
        </form>
    </section>

    <section class="card" aria-labelledby="add-staff-heading">
        <h2 id="add-staff-heading">Add Staff</h2>
        <form method="post" action="${pageContext.request.contextPath}/add-entity" novalidate>
            <input type="hidden" name="action" value="addStaff">
            <div class="field">
                <label for="staffFullName">Full Name</label>
                <input id="staffFullName" name="fullName" type="text" required>
            </div>
            <div class="field">
                <label for="staffEmail">Email</label>
                <input id="staffEmail" name="email" type="email" required>
            </div>
            <div class="field">
                <label for="staffPhone">Phone</label>
                <input id="staffPhone" name="phone" type="tel" required>
            </div>
            <div class="field">
                <label for="staffEmploymentDate">Employment Date (YYYY-MM-DD)</label>
                <input id="staffEmploymentDate" name="employmentDate" type="date" required>
            </div>
            <div class="field">
                <label for="staffDepartment">Department</label>
                <input id="staffDepartment" name="department" type="text" required>
            </div>
            <button type="submit" class="btn-primary">Add Staff</button>
        </form>
    </section>

    <section class="card" aria-labelledby="enroll-student-heading">
        <h2 id="enroll-student-heading">Enroll Student</h2>
        <form method="post" action="${pageContext.request.contextPath}/add-entity" novalidate>
            <input type="hidden" name="action" value="enrollStudent">
            <div class="field">
                <label for="enrollStudentId">Student ID</label>
                <input id="enrollStudentId" name="studentId" type="text" required>
            </div>
            <div class="field">
                <label for="enrollCourseCode">Course Code</label>
                <input id="enrollCourseCode" name="courseCode" type="text" required>
            </div>
            <div class="field">
                <label for="enrollDate">Enrollment Date (YYYY-MM-DD)</label>
                <input id="enrollDate" name="enrollmentDate" type="date" required>
            </div>
            <button type="submit" class="btn-primary">Enroll Student</button>
        </form>
    </section>

    <section class="card" aria-labelledby="assign-teacher-heading">
        <h2 id="assign-teacher-heading">Assign Teacher to Course</h2>
        <form method="post" action="${pageContext.request.contextPath}/add-entity" novalidate>
            <input type="hidden" name="action" value="assignTeacher">
            <div class="field">
                <label for="assignCourseCode">Course Code</label>
                <input id="assignCourseCode" name="courseCode" type="text" required>
            </div>
            <div class="field">
                <label for="assignTeacherId">Teacher ID</label>
                <input id="assignTeacherId" name="teacherId" type="text" required>
            </div>
            <button type="submit" class="btn-primary">Assign Teacher</button>
        </form>
    </section>

    <section class="card" aria-labelledby="record-grade-heading">
        <h2 id="record-grade-heading">Record Grade</h2>
        <form method="post" action="${pageContext.request.contextPath}/add-entity" novalidate>
            <input type="hidden" name="action" value="recordGrade">
            <div class="field">
                <label for="gradeStudentId">Student ID</label>
                <input id="gradeStudentId" name="studentId" type="text" required>
            </div>
            <div class="field">
                <label for="gradeCourseCode">Course Code</label>
                <input id="gradeCourseCode" name="courseCode" type="text" required>
            </div>
            <div class="field">
                <label for="assignmentName">Assignment Name</label>
                <input id="assignmentName" name="assignmentName" type="text" required>
            </div>
            <div class="field">
                <label for="gradeScore">Score</label>
                <input id="gradeScore" name="score" type="number" step="0.01" required>
            </div>
            <div class="field">
                <label for="gradeDate">Date (YYYY-MM-DD)</label>
                <input id="gradeDate" name="date" type="date" required>
            </div>
            <button type="submit" class="btn-primary">Record Grade</button>
        </form>
    </section>
</main>
<footer role="contentinfo" class="site-footer">
    <jsp:include page="footer.jsp"/>
    <p>&copy; ${pageContext.request.serverName} School Management</p>
</footer>
</body>
</html>
