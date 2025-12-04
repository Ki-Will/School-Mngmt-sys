package com.schoolmgmt.io;

import com.schoolmgmt.data.SchoolRecords;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileStorage {
    private final Path storagePath;

    public FileStorage(Path storagePath) {
        this.storagePath = Objects.requireNonNull(storagePath, "storagePath");
    }

    public synchronized void save(SchoolRecords records) {
        Objects.requireNonNull(records, "records");
        try {
            Path parent = storagePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Path tempDirectory = parent != null ? parent : storagePath.toAbsolutePath().getParent();
            if (tempDirectory == null) {
                tempDirectory = Path.of(".").toAbsolutePath();
            }

            Path tempFile = Files.createTempFile(tempDirectory, "school-records", ".tmp");
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tempFile))) {
                oos.writeObject(records);
            }
            try {
                Files.move(tempFile, storagePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempFile, storagePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save records", e);
        }
    }

    public synchronized SchoolRecords load() {
        if (!Files.exists(storagePath)) {
            return new SchoolRecords();
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(storagePath))) {
            Object result = ois.readObject();
            if (result instanceof SchoolRecords) {
                return (SchoolRecords) result;
            }
            throw new IllegalStateException("Invalid data format in storage file");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to load records", e);
        }
    }

    public Path getStoragePath() {
        return storagePath;
    }
}
