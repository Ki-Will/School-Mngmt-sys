package com.schoolmgmt.web;

import com.schoolmgmt.config.AppConfig;
import com.schoolmgmt.config.ApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(AppContextListener.class.getName());
    public static final String CONTEXT_ATTRIBUTE = "com.schoolmgmt.APPLICATION_CONTEXT";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            AppConfig config = AppConfig.load();
            ApplicationContext context = new ApplicationContext(config);
            ServletContext servletContext = sce.getServletContext();
            servletContext.setAttribute(CONTEXT_ATTRIBUTE, context);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize application context", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Object attribute = servletContext.getAttribute(CONTEXT_ATTRIBUTE);
        if (attribute instanceof ApplicationContext applicationContext) {
            applicationContext.close();
        }
    }
}
