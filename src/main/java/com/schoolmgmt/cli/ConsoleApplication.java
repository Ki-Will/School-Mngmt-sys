package com.schoolmgmt.cli;

import com.schoolmgmt.core.AutoSaveScheduler;
import com.schoolmgmt.data.SchoolRecords;
import com.schoolmgmt.io.SchoolRecordsStore;
import com.schoolmgmt.model.Course;
import com.schoolmgmt.model.GradeRecord;
import com.schoolmgmt.model.Staff;
import com.schoolmgmt.model.Student;
import com.schoolmgmt.model.Teacher;
import com.schoolmgmt.service.CourseService;
import com.schoolmgmt.service.StaffService;
import com.schoolmgmt.service.StudentService;
import com.schoolmgmt.service.TeacherService;
import com.schoolmgmt.util.IdGenerator;
import com.schoolmgmt.util.StudentAverageComparator;
import com.schoolmgmt.util.ValidationUtils;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
// import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleApplication {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DecimalFormat SCORE_FORMATTER = new DecimalFormat("0.00");

    private final SchoolRecords records;
    private final SchoolRecordsStore storage;
    private final AutoSaveScheduler scheduler;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final StaffService staffService;

    private final Scanner scanner;

    public ConsoleApplication(SchoolRecords records,
                              SchoolRecordsStore storage,
                              AutoSaveScheduler scheduler,
                              StudentService studentService,
                              TeacherService teacherService,
                              CourseService courseService,
                              StaffService staffService) {
        this.records = records;
        this.storage = storage;
        this.scheduler = scheduler;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.staffService = staffService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Select option: ");
            switch (choice) {
                case "1":
                    registerStudent();
                    break;
                case "2":
                    hireTeacher();
                    break;
                case "3":
                    addStaffMember();
                    break;
                case "4":
                    addCourse();
                    break;
                case "5":
                    assignTeacher();
                    break;
                case "6":
                    enrollStudent();
                    break;
                case "7":
                    recordGrade();
                    break;
                case "8":
                    listStudents();
                    break;
                case "9":
                    listCourses();
                    break;
                case "10":
                    listTeachers();
                    break;
                case "11":
                    listStaff();
                    break;
                case "12":
                    saveNow();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown option. Please try again.");
            }
            System.out.println();
        }

        scheduler.close();
        storage.save(records);
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("================ School Management System ================");
        System.out.println("1. Register student");
        System.out.println("2. Hire teacher");
        System.out.println("3. Add staff member");
        System.out.println("4. Add course");
        System.out.println("5. Assign teacher to course");
        System.out.println("6. Enroll student to course");
        System.out.println("7. Record grade for enrollment");
        System.out.println("8. List students (ranked by average)");
        System.out.println("9. List courses");
        System.out.println("10. List teachers");
        System.out.println("11. List staff members");
        System.out.println("12. Save records now");
        System.out.println("0. Exit");
    }

    private void registerStudent() {
        String name = readLine("Student full name: ");
        String email = readValidEmail();
        String phone = readValidPhone();
        LocalDate enrollmentDate = readDate("Enrollment date (yyyy-MM-dd): ");
        String id = IdGenerator.newId("STU");

        Student student = new Student(id, name, email, phone, enrollmentDate);
        studentService.registerStudent(student);
        System.out.println("Student registered with id: " + id);
    }

    private void hireTeacher() {
        String name = readLine("Teacher full name: ");
        String email = readLine("Email: ");
        String phone = readLine("Phone: ");
        LocalDate hireDate = readDate("Hire date (yyyy-MM-dd): ");

        String id = IdGenerator.newId("TEA");
        Teacher teacher = new Teacher(id, name, email, phone, hireDate);

        while (true) {
            String subject = readLine("Add expertise subject (leave blank to finish): ");
            if (subject.isBlank()) {
                break;
            }
            teacher.addExpertise(subject);
        }

        teacherService.hireTeacher(teacher);
        System.out.println("Teacher hired with id: " + id);
    }

    private void addStaffMember() {
        String name = readLine("Staff full name: ");
        String email = readLine("Email: ");
        String phone = readLine("Phone: ");
        LocalDate employmentDate = readDate("Employment date (yyyy-MM-dd): ");
        String department = readLine("Department: ");

        String id = IdGenerator.newId("STF");
        Staff staff = new Staff(id, name, email, phone, employmentDate, department);
        staffService.addStaffMember(staff);
        System.out.println("Staff member added with id: " + id);
    }

    private void addCourse() {
        String code = readLine("Course code: ").toUpperCase(Locale.ROOT);
        String title = readLine("Course title: ");
        long hours = readLong("Weekly duration in hours: ");

        Course course = new Course(code, title, Duration.ofHours(hours));
        courseService.addCourse(course);
        System.out.println("Course added: " + code);
    }

    private void assignTeacher() {
        String courseCode = readLine("Course code: ");
        String teacherId = readLine("Teacher id: ");
        courseService.assignTeacher(courseCode, teacherId);
        System.out.println("Teacher assigned to course.");
    }

    private void enrollStudent() {
        String studentId = readLine("Student id: ");
        String courseCode = readLine("Course code: ");
        studentService.enrollStudent(studentId, courseCode);
        System.out.println("Student enrolled.");
    }

    private void recordGrade() {
        String studentId = readLine("Student id: ");
        String courseCode = readLine("Course code: ");
        String assignment = readLine("Assignment/Exam name: ");
        double score = readDouble("Score (0-100): ");
        LocalDate date = readDate("Recorded date (yyyy-MM-dd): ");

        GradeRecord gradeRecord = new GradeRecord(assignment, score, date);
        studentService.recordGrade(studentId, courseCode, gradeRecord);
        System.out.println("Grade recorded.");
    }

    private void listStudents() {
        List<Student> students = new ArrayList<>(studentService.listStudents());
        students.sort(new StudentAverageComparator());

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("Students ranked by average grade:");
        for (int index = 0; index < students.size(); index++) {
            Student student = students.get(index);
            double average = student.calculateAverageGrade();
            System.out.printf(Locale.ROOT, "%d. %s (%s) - Avg: %s%n",
                    index + 1,
                    student.getFullName(),
                    student.getId(),
                    SCORE_FORMATTER.format(average));
            student.getEnrollments().forEach(enrollment ->
                    System.out.println("   • " + enrollment));
        }
    }

    private void listCourses() {
        List<Course> courses = new ArrayList<>(courseService.listCourses());
        courses.sort(Comparator.naturalOrder());

        if (courses.isEmpty()) {
            System.out.println("No courses defined.");
            return;
        }

        System.out.println("Courses:");
        courses.forEach(course -> System.out.printf(Locale.ROOT,
                "%s - %s (%d hours/week) Teacher: %s%n",
                course.getCode(),
                course.getTitle(),
                course.getWeeklyDuration().toHours(),
                Optional.ofNullable(course.getTeacherId()).orElse("Unassigned")));
    }

    private void listTeachers() {
        List<Teacher> teachers = new ArrayList<>(teacherService.listTeachers());
        teachers.sort(Comparator.comparing(Teacher::getFullName));

        if (teachers.isEmpty()) {
            System.out.println("No teachers recorded.");
            return;
        }

        System.out.println("Teachers:");
        teachers.forEach(teacher -> System.out.printf(Locale.ROOT,
                "%s - %s (Expertise: %s)%n",
                teacher.getId(),
                teacher.getFullName(),
                teacher.getExpertise().isEmpty() ? "None" : String.join(", ", teacher.getExpertise())));
    }

    private void listStaff() {
        List<Staff> staffMembers = new ArrayList<>(staffService.listStaff());
        staffMembers.sort(Comparator.comparing(Staff::getFullName));

        if (staffMembers.isEmpty()) {
            System.out.println("No staff members recorded.");
            return;
        }

        System.out.println("Staff members:");
        staffMembers.forEach(staff -> System.out.printf(Locale.ROOT,
                "%s - %s (%s Department)%n",
                staff.getId(),
                staff.getFullName(),
                staff.getDepartment()));
    }

    private void saveNow() {
        storage.save(records);
        System.out.println("Records saved manually.");
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid long value.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                double value = Double.parseDouble(input);
                if (value < 0 || value > 100) {
                    System.out.println("Score must be between 0 and 100.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid decimal value.");
            }
        }
    }

    private String readValidEmail() {
        while (true) {
            String input = readLine("Email: ");
            try {
                return ValidationUtils.requireValidEmail(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String readValidPhone() {
        while (true) {
            String input = readLine("Phone: ");
            try {
                return ValidationUtils.requireValidPhone(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
