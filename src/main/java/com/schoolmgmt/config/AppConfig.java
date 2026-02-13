package com.schoolmgmt.config;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

public record AppConfig(
        PersistenceMode persistenceMode,
        Path dataFilePath,
        String jdbcUrl,
        String jdbcUsername,
        String jdbcPassword,
        long autoSaveIntervalMillis
) {

    private static final String DEFAULT_DATA_FILE = "data/school-records.bin";
    private static final String DEFAULT_JDBC_URL = "jdbc:postgresql://localhost:5432/schoolmgmt_test";
    private static final String DEFAULT_JDBC_USERNAME = "postgres";
    private static final String DEFAULT_JDBC_PASSWORD = "postgres";
    private static final long DEFAULT_AUTO_SAVE_INTERVAL = 10_000L;

    public static AppConfig load() {
        PersistenceMode mode = resolvePersistenceMode();
        Path filePath = Path.of(DEFAULT_DATA_FILE);
        String jdbcUrl = DEFAULT_JDBC_URL;
        String jdbcUsername = DEFAULT_JDBC_USERNAME;
        String jdbcPassword = DEFAULT_JDBC_PASSWORD;
        long interval = DEFAULT_AUTO_SAVE_INTERVAL;

        return new AppConfig(mode, filePath, jdbcUrl, jdbcUsername, jdbcPassword, interval);
    }

    private static PersistenceMode resolvePersistenceMode() {
        String configured = resolveProperty("schoolmgmt.persistence", "SCHOOLMGMT_PERSISTENCE")
                .map(value -> value.toUpperCase(Locale.ROOT))
                .orElse(null);

        if (configured == null) {
            return PersistenceMode.JDBC;
        }

        try {
            return PersistenceMode.valueOf(configured);
        } catch (IllegalArgumentException ex) {
            return PersistenceMode.JDBC;
        }
    }

    private static Optional<String> resolveProperty(String systemProperty, String envVariable) {
        String sysValue = System.getProperty(systemProperty);
        if (sysValue != null && !sysValue.isBlank()) {
            return Optional.of(sysValue);
        }
        String envValue = System.getenv(envVariable);
        if (envValue != null && !envValue.isBlank()) {
            return Optional.of(envValue);
        }
        return Optional.empty();
    }
}
