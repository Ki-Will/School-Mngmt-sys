package com.schoolmgmt.config;

import com.schoolmgmt.cli.ConsoleApplication;
import com.schoolmgmt.core.AutoSaveScheduler;
import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.io.FileStorage;
import com.schoolmgmt.io.JdbcSchoolRecordsStore;
import com.schoolmgmt.io.SchoolRecordsStore;
import com.schoolmgmt.service.CourseService;
import com.schoolmgmt.service.StaffService;
import com.schoolmgmt.service.StudentService;
import com.schoolmgmt.service.TeacherService;
import com.schoolmgmt.service.UserAccountService;
import com.schoolmgmt.service.impl.CourseServiceImpl;
import com.schoolmgmt.service.impl.StaffServiceImpl;
import com.schoolmgmt.service.impl.StudentServiceImpl;
import com.schoolmgmt.service.impl.TeacherServiceImpl;
import com.schoolmgmt.service.impl.UserAccountServiceImpl;

public class ApplicationContext implements AutoCloseable {
    private final SchoolRecordsStore store;
    private final SchoolRecords records;
    private final AutoSaveScheduler autoSaveScheduler;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final StaffService staffService;
    private final UserAccountService userAccountService;

    public ApplicationContext(AppConfig config) {
        this.store = createStore(config);
        this.records = store.load();
        this.autoSaveScheduler = new AutoSaveScheduler(() -> store.save(records), config.autoSaveIntervalMillis());
        this.autoSaveScheduler.start();

        this.studentService = new StudentServiceImpl(records);
        this.teacherService = new TeacherServiceImpl(records);
        this.courseService = new CourseServiceImpl(records);
        this.staffService = new StaffServiceImpl(records);
        this.userAccountService = new UserAccountServiceImpl(records);

        userAccountService.findByUsername("admin")
                .orElseGet(() -> userAccountService.register("admin", "admin123", "ADMIN"));
    }

    private SchoolRecordsStore createStore(AppConfig config) {
        return switch (config.persistenceMode()) {
            case FILE -> new FileStorage(config.dataFilePath());
            case JDBC -> new JdbcSchoolRecordsStore(config.jdbcUrl(), config.jdbcUsername(), config.jdbcPassword());
        };
    }

    public SchoolRecords getRecords() {
        return records;
    }

    public SchoolRecordsStore getStore() {
        return store;
    }

    public AutoSaveScheduler getAutoSaveScheduler() {
        return autoSaveScheduler;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public TeacherService getTeacherService() {
        return teacherService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public StaffService getStaffService() {
        return staffService;
    }

    public UserAccountService getUserAccountService() {
        return userAccountService;
    }

    public ConsoleApplication createConsoleApplication() {
        return new ConsoleApplication(
                records,
                store,
                autoSaveScheduler,
                studentService,
                teacherService,
                courseService,
                staffService
        );
    }

    public void saveAndShutdown() {
        autoSaveScheduler.close();
        store.save(records);
    }

    @Override
    public void close() {
        saveAndShutdown();
    }
}
