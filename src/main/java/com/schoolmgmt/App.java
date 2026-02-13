package com.schoolmgmt;

import com.schoolmgmt.cli.ConsoleApplication;
import com.schoolmgmt.config.AppConfig;
import com.schoolmgmt.config.ApplicationContext;

public class App {
    public static void main(String[] args) {
        AppConfig config = AppConfig.load();

        try (ApplicationContext context = new ApplicationContext(config)) {
            ConsoleApplication application = context.createConsoleApplication();
            application.run();
        }
    }
}
