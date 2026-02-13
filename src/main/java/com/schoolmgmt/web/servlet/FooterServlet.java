package com.schoolmgmt.web.servlet;

import com.schoolmgmt.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FooterServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> recentActivities = getRecentActivities();
        req.setAttribute("recentActivities", recentActivities);
        req.getRequestDispatcher("/WEB-INF/views/footer.jsp").forward(req, resp);
    }

    private List<String> getRecentActivities() {
        List<String> activities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("activities.log"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                activities.add(line);
            }
        } catch (IOException e) {
            // File not found or error, return empty
        }
        // Get last 5 activities
        if (activities.size() > 5) {
            activities = activities.subList(activities.size() - 5, activities.size());
        }
        // Reverse to show latest first
        Collections.reverse(activities);
        return activities;
    }
}
