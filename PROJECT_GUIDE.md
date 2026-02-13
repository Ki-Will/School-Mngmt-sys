# School Management CLI – Project Guide

## Overview
A console-based School Management System showcasing advanced Java concepts:
- Object-oriented design with inheritance, encapsulation, polymorphism, and abstraction.
- Collections, generics, Comparable/Comparator implementations, streams, and lambdas.
- Thread-based auto-saving with robust file serialization and I/O handling.
- Validation, exception management, formatting utilities, and reusable services.

Use this guide to understand structure, operations, and implementation details.

## Project Structure
```
JavaProject/
├─ src/
│  └─ com/schoolmgmt/
│     ├─ App.java                 # Entry point wiring CLI + services + persistence
│     ├─ cli/
│     │  └─ ConsoleApplication.java
│     ├─ core/
│     │  └─ AutoSaveScheduler.java
│     ├─ data/
│     │  └─ SchoolRecords.java
│     ├─ io/
│     │  └─ FileStorage.java
│     ├─ model/
│     │  ├─ Course.java
│     │  ├─ Enrollment.java
│     │  ├─ GradeRecord.java
│     │  ├─ Identifiable.java
│     │  ├─ Person.java
│     │  ├─ Staff.java
│     │  ├─ Student.java
│     │  └─ Teacher.java
│     ├─ repository/
│     │  ├─ CrudRepository.java
│     │  └─ InMemoryRepository.java
│     ├─ service/
│     │  ├─ CourseService.java
│     │  ├─ StaffService.java
│     │  ├─ StudentService.java
│     │  ├─ TeacherService.java
│     │  └─ impl/
│     │     ├─ CourseServiceImpl.java
│     │     ├─ StaffServiceImpl.java
│     │     ├─ StudentServiceImpl.java
│     │     └─ TeacherServiceImpl.java
│     └─ util/
│        ├─ IdGenerator.java
│        ├─ StudentAverageComparator.java
│        └─ ValidationUtils.java
├─ data/
│  └─ school-records.bin (created after first run)
├─ out/                      # Build output (generated)
└─ run.ps1                   # PowerShell helper to compile/run
```

## How to Build & Run
1. **Compile only**
   ```powershell
   Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass; & .\run.ps1 -CompileOnly
   ```
2. **Compile and start CLI**
   ```powershell
   Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass; & .\run.ps1
   ```
3. **Manual commands**
   ```powershell
   javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
   java -cp out com.schoolmgmt.App
   ```

## Application Flow
1. `App` boots the system, loading persisted `SchoolRecords`, starting the `AutoSaveScheduler`, and constructing services and the `ConsoleApplication`.
2. `ConsoleApplication` presents a menu loop, routing user choices to operations:
   - Register students/teachers/staff.
   - Manage courses and assignments.
   - Enroll students, record grades, and list entities.
   - Trigger manual save or exit (persists data and shuts down thread).
3. `AutoSaveScheduler` runs every 10 seconds to persist in-memory state without blocking the CLI.

## Supported Operations
- **Student management**: register, list (ranked by GPA), enroll in courses, record grades.
- **Teacher management**: hire teachers, manage expertise list, assign to courses.
- **Staff management**: add staff members with departmental info.
- **Course management**: add courses, assign teachers, list sorted catalog.
- **Persistence**: manual save (menu option 12) and automatic background saving.

## Key Implementation Highlights
### Object-Oriented Design (@com/schoolmgmt/model/Person.java#5-75)
- `Person` is an abstract superclass (abstraction) with shared fields and behaviors.
- `Student`, `Teacher`, `Staff` extend Person (inheritance) and provide role-specific logic.
- Fields are private with getters/setters (encapsulation); behaviors like `calculateAverageGrade` demonstrate polymorphism in usage contexts.

### Comparable & Comparator
- `Person` and `Course` implement `Comparable` for consistent ordering (@com/schoolmgmt/model/Person.java#51-58, @com/schoolmgmt/model/Course.java#33-44).
- `StudentAverageComparator` ranks students by average grade (@com/schoolmgmt/util/StudentAverageComparator.java#1-13).

### Collections & Generics
- Lists and sets manage enrollments and expertise (@com/schoolmgmt/model/Student.java#12-29, @com/schoolmgmt/model/Teacher.java#11-29).
- `InMemoryRepository<ID, T extends Identifiable<ID>>` leverages generics and an internal `Map` for entity storage (@com/schoolmgmt/repository/InMemoryRepository.java#11-36).

### Streams & Lambdas
- Stream pipelines compute averages and filter enrollments (@com/schoolmgmt/model/Student.java#32-38, @com/schoolmgmt/service/impl/StudentServiceImpl.java#62-68).
- Lambdas and method references keep collection processing expressive.

### Threads & Concurrency
- `AutoSaveScheduler` runs a daemon worker thread to periodically invoke persistence without blocking user actions (@com/schoolmgmt/core/AutoSaveScheduler.java#10-37).

### Serialization & File I/O
- `SchoolRecords` aggregates repositories and implements `Serializable` (@com/schoolmgmt/data/SchoolRecords.java#12-38).
- `FileStorage` writes to a temp file and moves atomically when supported, ensuring durable saves even on Windows (@com/schoolmgmt/io/FileStorage.java#21-44).

### Validation & Regex
- `ValidationUtils` uses compiled regex to enforce email/phone formats (@com/schoolmgmt/util/ValidationUtils.java#7-33).
- CLI uses helper methods to loop until valid input is entered (@com/schoolmgmt/cli/ConsoleApplication.java#341-360).

### Formatting & IDs
- IDs generated via timestamp + counter with `DecimalFormat` and `DateTimeFormatter` (@com/schoolmgmt/util/IdGenerator.java#7-21).
- Console output uses `String.format`/`printf` for readable reports (@com/schoolmgmt/cli/ConsoleApplication.java#217-239).

### Exception Handling
- Services guard against duplicates/missing data via `IllegalArgumentException`/`IllegalStateException` (@com/schoolmgmt/service/impl/StudentServiceImpl.java#22-55).
- Storage wraps IO exceptions with contextual messages (@com/schoolmgmt/io/FileStorage.java#39-44).
- CLI input parsing catches number/date format errors and prompts again (@com/schoolmgmt/cli/ConsoleApplication.java#305-360).

## Data Persistence
- On first run, `data/school-records.bin` is created. Subsequent launches load the persisted `SchoolRecords` instance.
- Auto-save every 10 seconds ensures in-memory changes are durably stored.

## Extending the Project
- Add new services or repositories by implementing the `CrudRepository` contract.
- Introduce new CLI actions by extending the menu switch in `ConsoleApplication`.
- Hook new threads or scheduled tasks via additional `AutoSaveScheduler` instances.

## Troubleshooting
- **Script blocked**: Use the provided `Set-ExecutionPolicy -Scope Process` prefix before calling `run.ps1`.
- **Directory warning**: If the IDE flags an empty `src/model` folder, delete the empty directory.
- **Serialization errors**: Ensure classes remain `Serializable`; update `serialVersionUID` only when structure changes.

## Summary
This project demonstrates a robust, modular CLI application that integrates core Java features—object orientation, collections, generics, streams, threading, regex validation, exception handling, and I/O/serialization—within a cohesive school management domain. Use this guide as a quick reference for architecture, usage, and extension points.
