<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="footer-activities">
    <h3>Recent Activities</h3>
    <div class="scrollable-activities">
        <ul>
            <c:forEach var="activity" items="${recentActivities}">
                <li>${activity}</li>
            </c:forEach>
        </ul>
    </div>
</div>

<style>
.footer-activities {
    margin-top: 2rem;
    padding: 1rem;
    background-color: #f8f9fa;
    border-top: 1px solid #e9ecef;
}

.footer-activities h3 {
    margin-top: 0;
    font-size: 1rem;
}

.scrollable-activities {
    max-height: 150px;
    overflow-y: auto;
}

.scrollable-activities ul {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.scrollable-activities li {
    padding: 0.25rem 0;
    border-bottom: 1px solid #e9ecef;
    font-size: 0.875rem;
}
</style>
