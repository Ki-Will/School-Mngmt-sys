<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>School Management – Sign Up</title>
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

        .auth-main {
            max-width: min(1100px, 92vw);
            margin: calc(var(--nav-height) + 1.5rem) auto 3rem;
            padding: 0 clamp(1rem, 4vw, 2.5rem);
            position: relative;
        }

        .auth-main::before {
            content: "";
            position: absolute;
            inset: 0;
            background: radial-gradient(circle at 20% 20%, rgba(37, 99, 235, 0.18), transparent 45%),
            radial-gradient(circle at 80% 0%, rgba(14, 165, 233, 0.12), transparent 60%);
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
            max-width: 480px;
            margin-inline: auto;
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

        input,
        select {
            padding: 0.75rem 1rem;
            border-radius: 12px;
            border: 1px solid var(--border);
            font-size: 1rem;
            background: rgba(255, 255, 255, 0.92);
            transition: border-color var(--transition), box-shadow var(--transition), background var(--transition);
        }

        input:focus,
        select:focus,
        button:focus {
            outline: 3px solid rgba(37, 99, 235, 0.35);
            outline-offset: 2px;
        }

        .field input:focus,
        .field select:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
            background: #fff;
        }

        .field input:hover,
        .field select:hover {
            border-color: var(--border-strong);
        }

        .actions {
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            gap: 1rem;
            justify-content: space-between;
        }

        .btn-primary {
            background: var(--primary);
            border: none;
            color: #fff;
            padding: 0.75rem 1.5rem;
            border-radius: 12px;
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

        .alert {
            background: var(--alert);
            color: var(--alert-text);
            padding: 0.75rem 1rem;
            border-radius: 10px;
            margin-bottom: 1.5rem;
            border: 1px solid rgba(185, 28, 28, 0.25);
        }

        @media (prefers-reduced-motion: reduce) {
            *, *::before, *::after {
                animation-duration: 0.01ms !important;
                animation-iteration-count: 1 !important;
                transition-duration: 0.01ms !important;
                scroll-behavior: auto !important;
            }
        }

        @media (max-width: 768px) {
            .auth-main {
                margin-top: 1.5rem;
            }
        }
    </style>
</head>
<body>
<header class="site-header" role="banner">
    <h1>Create an Account</h1>
    <p class="subtitle">Join the school management portal</p>
</header>
<main role="main" class="auth-main">
    <section class="card" aria-labelledby="signup-heading">
        <h2 id="signup-heading">Register</h2>
        <c:if test="${not empty error}">
            <div class="alert" role="alert" aria-live="assertive">${error}</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/signup" novalidate>
            <div class="field">
                <label for="username">Username</label>
                <input id="username" name="username" type="text" required autocomplete="username" value="${param.username}">
            </div>
            <div class="field">
                <label for="password">Password</label>
                <input id="password" name="password" type="password" required autocomplete="new-password">
            </div>
            <div class="field">
                <label for="confirmPassword">Confirm password</label>
                <input id="confirmPassword" name="confirmPassword" type="password" required autocomplete="new-password">
            </div>
            <div class="field">
                <label for="role">Role</label>
                <select id="role" name="role" required>
                    <option value="">Choose a role</option>
                    <option value="ADMIN" ${param.role == 'ADMIN' ? 'selected' : ''}>Administrator</option>
                    <option value="STAFF" ${param.role == 'STAFF' ? 'selected' : ''}>Staff</option>
                </select>
            </div>
            <div class="actions">
                <button type="submit" class="btn-primary">Create account</button>
                <a class="secondary-link" href="${pageContext.request.contextPath}/login">Already have an account?</a>
            </div>
        </form>
    </section>
</main>
<footer role="contentinfo" class="site-footer">
    <p>&copy; ${pageContext.request.serverName} School Management</p>
</footer>
</body>
</html>
