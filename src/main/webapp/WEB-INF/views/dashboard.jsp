<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>School Management – Dashboard</title>

    <style>
        :root {
            --bg: #eef2ff;
            --surface: rgba(255, 255, 255, 0.95);
            --surface-strong: #ffffff;
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --primary-muted: rgba(37, 99, 235, 0.12);
            --text: #0f172a;
            --muted: #64748b;
            --border: rgba(148, 163, 184, 0.25);
            --shadow-soft: 0 10px 25px rgba(15, 23, 42, 0.08);
            --shadow-strong: 0 16px 40px rgba(15, 23, 42, 0.12);
            --transition: 160ms ease;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Segoe UI", Arial, sans-serif;
            background: var(--bg);
            color: var(--text);
            line-height: 1.5;
        }

        /* ===== Header (Reduced Size) ===== */

        .layout .site-header {
            background: var(--surface-strong);
            border-bottom: 1px solid var(--border);
            padding: 0.6rem 1.5rem; /* reduced */
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 1rem;
        }

        .brand h1 {
            margin: 0;
            font-size: 1.6rem; /* reduced */
        }

        .subtitle {
            margin: 0;
            font-size: 0.9rem;
            color: var(--muted);
        }

        .site-nav ul {
            list-style: none;
            margin: 0;
            padding: 0;
            display: flex;
            gap: 1rem;
        }

        .site-nav a {
            color: var(--muted);
            text-decoration: none;
            font-weight: 600;
            padding: 0.4rem 0.8rem;
            border-radius: 8px;
            transition: all var(--transition);
        }

        .site-nav a.active,
        .site-nav a:hover {
            color: var(--primary);
            background: var(--primary-muted);
        }

        .btn-secondary {
            background: transparent;
            color: var(--primary);
            border: 1px solid var(--primary);
            padding: 0.4rem 1rem;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
        }

        .btn-secondary:hover {
            background: var(--primary-muted);
        }

        /* ===== Main ===== */

        .dashboard-main {
            max-width: 1100px;
            margin: 1.5rem auto 2rem; /* reduced spacing */
            padding: 0 1.5rem;
        }

        /* ===== Cards ===== */

        .cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.2rem;
            margin-bottom: 1.5rem;
        }

        .card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 1.4rem;
            box-shadow: var(--shadow-soft);
            transition: transform var(--transition), box-shadow var(--transition);
        }

        .card:hover {
            transform: translateY(-3px);
            box-shadow: var(--shadow-strong);
        }

        .card.metric {
            background: rgba(37, 99, 235, 0.05);
        }

        .metric-value {
            font-size: 2rem;
            font-weight: 700;
            margin-top: 0.4rem;
        }

        /* ===== Table ===== */

        .responsive-table {
            overflow-x: auto;
            border-radius: 12px;
            border: 1px solid var(--border);
            background: var(--surface);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            text-align: left;
            padding: 0.75rem 1rem;
            border-bottom: 1px solid var(--border);
        }

        tbody tr:hover {
            background: rgba(37, 99, 235, 0.06);
        }

        .empty-state {
            color: var(--muted);
        }

        /* ===== Footer ===== */

        .site-footer {
            border-top: 1px solid var(--border);
            text-align: center;
            font-size: 0.85rem;
            color: var(--muted);
            padding: 1.5rem;
            background: var(--surface-strong);
        }

        @media (max-width: 768px) {
            .layout .site-header {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>

<body class="layout">

<header class="site-header">
    <div class="brand">
        <h1>School Management Portal</h1>
        <p class="subtitle">
            Welcome,
            <c:out value="${currentUser != null ? currentUser.username : 'User'}"/>
        </p>
    </div>

    <nav class="site-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/dashboard" class="active">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/students">Students</a></li>
            <li><a href="${pageContext.request.contextPath}/teachers">Teachers</a></li>
            <li><a href="${pageContext.request.contextPath}/courses">Courses</a></li>
            <li><a href="${pageContext.request.contextPath}/staff">Staff</a></li>

            <c:if test="${sessionScope.isAdmin == true}">
                <li><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
            </c:if>
        </ul>
    </nav>

    <form method="post" action="${pageContext.request.contextPath}/logout">
        <button type="submit" class="btn-secondary">Sign out</button>
    </form>
</header>

<main class="dashboard-main">

    <!-- Metrics -->
    <section class="cards">
        <article class="card metric">
            <h2>Total Students</h2>
            <p class="metric-value">
                <c:out value="${studentCount}" default="0"/>
            </p>
        </article>

        <article class="card metric">
            <h2>Total Teachers</h2>
            <p class="metric-value">
                <c:out value="${teacherCount}" default="0"/>
            </p>
        </article>

        <article class="card metric">
            <h2>Active Courses</h2>
            <p class="metric-value">
                <c:out value="${courseCount}" default="0"/>
            </p>
        </article>

        <article class="card metric">
            <h2>Staff Members</h2>
            <p class="metric-value">
                <c:out value="${staffCount}" default="0"/>
            </p>
        </article>
    </section>

    <!-- Recent Activities -->
    <section class="card">
        <h2>Recent Enrollments</h2>

        <c:choose>
            <c:when test="${not empty recentEnrollments}">
                <div class="responsive-table">
                    <table>
                        <thead>
                        <tr>
                            <th>Student</th>
                            <th>Course</th>
                            <th>Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="enrollment" items="${recentEnrollments}">
                            <tr>
                                <td><c:out value="${enrollment.studentName}"/></td>
                                <td><c:out value="${enrollment.courseTitle}"/></td>
                                <td><c:out value="${enrollment.enrollmentDate}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>

            <c:otherwise>
                <p class="empty-state">
                    No enrollments recorded yet.
                </p>
            </c:otherwise>
        </c:choose>
    </section>

</main>

<footer class="site-footer">
    <jsp:include page="footer.jsp"/>
    <p>&copy; ${pageContext.request.serverName} School Management</p>
</footer>

</body>
</html>
