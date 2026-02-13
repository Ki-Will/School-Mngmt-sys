<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Access Denied</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
        }
        h1 {
            color: #d9534f;
        }
    </style>
</head>
<body>
    <h1>Access Denied</h1>
    <p>You do not have permission to access this page.</p>
    <a href="${pageContext.request.contextPath}/dashboard">Go back to Dashboard</a>
</body>
<footer role="contentinfo" class="site-footer">
    <jsp:include page="/footer" />
    <p>&copy; ${pageContext.request.serverName} School Management</p>
</footer>
</html>
