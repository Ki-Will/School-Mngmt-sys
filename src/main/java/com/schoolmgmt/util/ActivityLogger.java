package com.schoolmgmt.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class ActivityLogger {

    private static final String LOG_FILE = "activities.log"; // relative to webapp root

    public static void logActivity(String activity, String doneBy, String response) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().toString();
            writer.println(timestamp + "," + activity + "," + doneBy + "," + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
