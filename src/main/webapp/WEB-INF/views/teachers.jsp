<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>School Management – Teachers</title>
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
            --nav-height: 3rem;
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

        .layout .site-header {
            background: var(--surface-strong);
            border-bottom: 1px solid var(--border);
            padding: 1.75rem clamp(1.5rem, 5vw, 3rem);
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 1.5rem;
            backdrop-filter: blur(16px);
            -webkit-backdrop-filter: blur(16px);
        }

        .brand h1 {
            margin: 0;
            font-size: clamp(1.6rem, 2.4vw, 2.1rem);
        }

        .subtitle {
            margin: 0.25rem 0 0;
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
            padding: 0.65rem 1rem;
            border-radius: 12px;
            transition: color var(--transition), background var(--transition), transform var(--transition);
        }

        .site-nav a.active,
        .site-nav a:hover,
        .site-nav a:focus {
            color: var(--primary);
            background: var(--primary-muted);
            transform: translateY(-2px);
        }

        form[action$="/logout"] {
            margin-left: auto;
        }

        .btn-secondary {
            background: transparent;
            color: var(--primary);
            border: 1px solid var(--primary);
            padding: 0.6rem 1.4rem;
            border-radius: 12px;
            font-weight: 600;
            cursor: pointer;
            transition: transform var(--transition), color var(--transition), border-color var(--transition), background var(--transition);
        }

        .btn-secondary:hover,
        .btn-secondary:focus {
            color: var(--primary-hover);
            border-color: var(--primary-hover);
            background: rgba(37, 99, 235, 0.06);
            transform: translateY(-1px);
        }

        .main-content {
            max-width: min(1200px, 92vw);
            margin: calc(var(--nav-height) + 1.5rem) auto 3rem;
            padding: 0 clamp(1rem, 4vw, 2.5rem);
            position: relative;
        }

        .main-content::before {
            content: "";
            position: absolute;
            inset: -80px -120px 0;
            background: radial-gradient(circle at 0 0, rgba(14, 165, 233, 0.16), transparent 45%),
            radial-gradient(circle at 100% 20%, rgba(79, 70, 229, 0.12), transparent 55%);
            z-index: -1;
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

        .card:hover,
        .card:focus-within {
            transform: translateY(-4px);
            border-color: var(--border-strong);
            box-shadow: var(--shadow-strong);
        }

        .section-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 1rem;
            margin-bottom: 1rem;
        }

        .responsive-table {
            overflow-x: auto;
            border-radius: 16px;
            border: 1px solid var(--border);
            background: var(--surface);
            box-shadow: var(--shadow-soft);
        }

        .responsive-table table {
            width: 100%;
            border-collapse: collapse;
        }

        .responsive-table th,
        .responsive-table td {
            text-align: left;
            padding: 0.75rem 1rem;
            border-bottom: 1px solid var(--border);
            transition: background var(--transition);
        }

        .responsive-table tbody tr:hover {
            background: rgba(37, 99, 235, 0.06);
        }

        .empty-state {
            color: var(--muted);
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

        @media (max-width: 768px) {
            .layout .site-header {
                flex-direction: column;
                align-items: stretch;
            }

            .site-nav ul {
                flex-wrap: wrap;
                justify-content: center;
            }

            .main-content {
                margin-top: 1.5rem;
            }
        }
    </style>
</head>
<body class="layout">
<header class="site-header" role="banner">
    <div class="brand">
        <h1 aria-label="School Management Portal home">School Management Portal</h1>
        <p class="subtitle">Welcome, <c:out value="${currentUser.username}" default="User"/>!</p>
        <a class="secondary-link" href="${pageContext.request.contextPath}/dashboard">← Back to Dashboard</a>
    </div>
    <nav class="site-nav" aria-label="Primary">
        <ul>
            <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/students">Students</a></li>
            <li><a href="${pageContext.request.contextPath}/teachers" class="active">Teachers</a></li>
            <li><a href="${pageContext.request.contextPath}/courses">Courses</a></li>
            <li><a href="${pageContext.request.contextPath}/staff">Staff</a></li>
            <c:if test="${sessionScope.isAdmin}">
                <li><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
            </c:if>
        </ul>
    </nav>
    <form method="post" action="${pageContext.request.contextPath}/logout">
        <button type="submit" class="btn-secondary">Sign out</button>
    </form>
</header>
<main role="main" class="main-content">
    <section class="card" aria-labelledby="teachers-list">
        <div class="section-header">
            <h2 id="teachers-list">All Teachers</h2>
        </div>
        <c:choose>
            <c:when test="${not empty teachers}">
                <div class="responsive-table" role="region" aria-live="polite">
                    <table>
                        <thead>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">Full Name</th>
                            <th scope="col">Email</th>
                            <th scope="col">Phone</th>
                            <th scope="col">Hire Date</th>
                            <th scope="col">Expertise</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="teacher" items="${teachers}">
                            <tr>
                                <td><c:out value="${teacher.id}"/></td>
                                <td><c:out value="${teacher.fullName}"/></td>
                                <td><c:out value="${teacher.email}"/></td>
                                <td><c:out value="${teacher.phoneNumber}"/></td>
                                <td><c:out value="${teacher.hireDate}"/></td>
                                <td><c:forEach var="exp" items="${teacher.expertise}" varStatus="status"><c:out value="${exp}"/><c:if test="${!status.last}">, </c:if></c:forEach></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p class="empty-state">No teachers hired yet.</p>
            </c:otherwise>
        </c:choose>
    </section>
</main>
<footer role="contentinfo" class="site-footer">
    <jsp:include page="footer.jsp" />
    <p>&copy; ${pageContext.request.serverName} School Management</p>
</footer>
</body>
</html>
