package com.schoolmgmt.io;

import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.model.Course;
import com.schoolmgmt.model.Enrollment;
import com.schoolmgmt.model.GradeRecord;
import com.schoolmgmt.model.Staff;
import com.schoolmgmt.model.Student;
import com.schoolmgmt.model.Teacher;
import com.schoolmgmt.model.UserAccount;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Objects;

public class JdbcSchoolRecordsStore implements SchoolRecordsStore {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public JdbcSchoolRecordsStore(String jdbcUrl, String username, String password) {
        this.jdbcUrl = Objects.requireNonNull(jdbcUrl, "jdbcUrl");
        this.username = username;
        this.password = password;
        initializeSchema();
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("PostgreSQL driver not found", e);
        }
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private void initializeSchema() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id VARCHAR(64) PRIMARY KEY, " +
                    "full_name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL, " +
                    "phone VARCHAR(64) NOT NULL, " +
                    "enrollment_date DATE NOT NULL)");

            statement.execute("CREATE TABLE IF NOT EXISTS teachers (" +
                    "id VARCHAR(64) PRIMARY KEY, " +
                    "full_name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL, " +
                    "phone VARCHAR(64) NOT NULL, " +
                    "hire_date DATE NOT NULL)");

            statement.execute("CREATE TABLE IF NOT EXISTS teacher_expertise (" +
                    "teacher_id VARCHAR(64) NOT NULL, " +
                    "subject VARCHAR(128) NOT NULL, " +
                    "PRIMARY KEY (teacher_id, subject), " +
                    "FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE)");

            statement.execute("CREATE TABLE IF NOT EXISTS courses (" +
                    "code VARCHAR(32) PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "weekly_hours INT NOT NULL, " +
                    "teacher_id VARCHAR(64), " +
                    "FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL)");

            statement.execute("CREATE TABLE IF NOT EXISTS staff (" +
                    "id VARCHAR(64) PRIMARY KEY, " +
                    "full_name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL, " +
                    "phone VARCHAR(64) NOT NULL, " +
                    "employment_date DATE NOT NULL, " +
                    "department VARCHAR(128) NOT NULL)");

            statement.execute("CREATE TABLE IF NOT EXISTS enrollments (" +
                    "id VARCHAR(128) PRIMARY KEY, " +
                    "student_id VARCHAR(64) NOT NULL, " +
                    "course_code VARCHAR(32) NOT NULL, " +
                    "enrollment_date DATE NOT NULL, " +
                    "assignment_name VARCHAR(255), " +
                    "grade_score double precision, " +
                    "grade_date DATE, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (course_code) REFERENCES courses(code) ON DELETE CASCADE)");

            statement.execute("CREATE TABLE IF NOT EXISTS user_accounts (" +
                    "id VARCHAR(64) PRIMARY KEY, " +
                    "username VARCHAR(128) NOT NULL UNIQUE, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(64) NOT NULL)");
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to initialise database schema", e);
        }
    }

    @Override
    public synchronized SchoolRecords load() {
        SchoolRecords records = new SchoolRecords();
        try (Connection connection = getConnection()) {
            loadStudents(connection, records);
            loadTeachers(connection, records);
            loadCourses(connection, records);
            loadStaff(connection, records);
            loadUserAccounts(connection, records);
            loadEnrollments(connection, records);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to load records from database", e);
        }
        return records;
    }

    private void loadStudents(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, full_name, email, phone, enrollment_date FROM students")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getString("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getDate("enrollment_date").toLocalDate()
                    );
                    records.getStudents().save(student);
                }
            }
        }
    }

    private void loadTeachers(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, full_name, email, phone, hire_date FROM teachers")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Teacher teacher = new Teacher(
                            rs.getString("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getDate("hire_date").toLocalDate()
                    );
                    records.getTeachers().save(teacher);
                }
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT teacher_id, subject FROM teacher_expertise")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String subject = rs.getString("subject");
                    records.getTeachers()
                            .findById(rs.getString("teacher_id"))
                            .ifPresent(teacher -> teacher.addExpertise(subject));
                }
            }
        }
    }

    private void loadCourses(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT code, title, weekly_hours, teacher_id FROM courses")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                            rs.getString("code"),
                            rs.getString("title"),
                            Duration.ofHours(rs.getInt("weekly_hours"))
                    );
                    String teacherId = rs.getString("teacher_id");
                    if (teacherId != null) {
                        course.assignTeacher(teacherId);
                    }
                    records.getCourses().save(course);
                }
            }
        }
    }

    private void loadStaff(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, full_name, email, phone, employment_date, department FROM staff")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Staff staff = new Staff(
                            rs.getString("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getDate("employment_date").toLocalDate(),
                            rs.getString("department")
                    );
                    records.getStaff().save(staff);
                }
            }
        }
    }

    private void loadUserAccounts(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, username, password_hash, role FROM user_accounts")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserAccount account = new UserAccount(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("role")
                    );
                    records.getUserAccounts().save(account);
                }
            }
        }
    }

    private void loadEnrollments(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, student_id, course_code, enrollment_date, assignment_name, grade_score, grade_date FROM enrollments")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment(
                            rs.getString("student_id"),
                            rs.getString("course_code"),
                            rs.getDate("enrollment_date").toLocalDate()
                    );
                    String assignment = rs.getString("assignment_name");
                    if (assignment != null) {
                        Date sqlGradeDate = rs.getDate("grade_date");
                        if (sqlGradeDate != null) {
                            Double gradeScore = rs.getObject("grade_score", Double.class);
                            GradeRecord gradeRecord = new GradeRecord(
                                    assignment,
                                    gradeScore != null ? gradeScore : 0.0,
                                    sqlGradeDate.toLocalDate()
                            );
                            enrollment.setGradeRecord(gradeRecord);
                        }
                    }
                    records.getEnrollments().save(enrollment);
                    records.getStudents()
                            .findById(rs.getString("student_id"))
                            .ifPresent(student -> student.addEnrollment(enrollment));
                }
            }
        }
    }

    @Override
    public synchronized void save(SchoolRecords records) {
        Objects.requireNonNull(records, "records");
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                clearTables(connection);
                saveStudents(connection, records);
                saveTeachers(connection, records);
                saveCourses(connection, records);
                saveStaff(connection, records);
                saveUserAccounts(connection, records);
                saveEnrollments(connection, records);
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to save records to database", e);
        }
    }

    private void clearTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM enrollments");
            statement.executeUpdate("DELETE FROM teacher_expertise");
            statement.executeUpdate("DELETE FROM courses");
            statement.executeUpdate("DELETE FROM staff");
            statement.executeUpdate("DELETE FROM students");
            statement.executeUpdate("DELETE FROM teachers");
            statement.executeUpdate("DELETE FROM user_accounts");
        }
    }

    private void saveStudents(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO students (id, full_name, email, phone, enrollment_date) VALUES (?, ?, ?, ?, ?)")) {
            for (Student student : records.getStudents().findAll()) {
                ps.setString(1, student.getId());
                ps.setString(2, student.getFullName());
                ps.setString(3, student.getEmail());
                ps.setString(4, student.getPhoneNumber());
                ps.setDate(5, Date.valueOf(student.getEnrollmentDate()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveTeachers(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO teachers (id, full_name, email, phone, hire_date) VALUES (?, ?, ?, ?, ?)")) {
            for (Teacher teacher : records.getTeachers().findAll()) {
                ps.setString(1, teacher.getId());
                ps.setString(2, teacher.getFullName());
                ps.setString(3, teacher.getEmail());
                ps.setString(4, teacher.getPhoneNumber());
                ps.setDate(5, Date.valueOf(teacher.getHireDate()));
                ps.addBatch();
            }
            ps.executeBatch();
        }

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO teacher_expertise (teacher_id, subject) VALUES (?, ?)")) {
            for (Teacher teacher : records.getTeachers().findAll()) {
                for (String subject : teacher.getExpertise()) {
                    ps.setString(1, teacher.getId());
                    ps.setString(2, subject);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void saveCourses(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO courses (code, title, weekly_hours, teacher_id) VALUES (?, ?, ?, ?)")) {
            for (Course course : records.getCourses().findAll()) {
                ps.setString(1, course.getCode());
                ps.setString(2, course.getTitle());
                ps.setInt(3, (int) course.getWeeklyDuration().toHours());
                ps.setString(4, course.getTeacherId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveStaff(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO staff (id, full_name, email, phone, employment_date, department) VALUES (?, ?, ?, ?, ?, ?)")) {
            for (Staff staff : records.getStaff().findAll()) {
                ps.setString(1, staff.getId());
                ps.setString(2, staff.getFullName());
                ps.setString(3, staff.getEmail());
                ps.setString(4, staff.getPhoneNumber());
                ps.setDate(5, Date.valueOf(staff.getEmploymentDate()));
                ps.setString(6, staff.getDepartment());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveUserAccounts(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO user_accounts (id, username, password_hash, role) VALUES (?, ?, ?, ?)")) {
            for (UserAccount account : records.getUserAccounts().findAll()) {
                ps.setString(1, account.getId());
                ps.setString(2, account.getUsername());
                ps.setString(3, account.getPasswordHash());
                ps.setString(4, account.getRole());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveEnrollments(Connection connection, SchoolRecords records) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO enrollments (id, student_id, course_code, enrollment_date, assignment_name, grade_score, grade_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            for (Enrollment enrollment : records.getEnrollments().findAll()) {
                ps.setString(1, enrollment.getId());
                ps.setString(2, enrollment.getStudentId());
                ps.setString(3, enrollment.getCourseCode());
                ps.setDate(4, Date.valueOf(enrollment.getEnrollmentDate()));
                GradeRecord gradeRecord = enrollment.getGradeRecord();
                if (gradeRecord != null) {
                    ps.setString(5, gradeRecord.getAssignmentName());
                    ps.setDouble(6, gradeRecord.getScore());
                    ps.setDate(7, gradeRecord.getRecordedDate() != null ? Date.valueOf(gradeRecord.getRecordedDate()) : null);
                } else {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                    ps.setNull(6, java.sql.Types.DOUBLE);
                    ps.setNull(7, java.sql.Types.DATE);
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
