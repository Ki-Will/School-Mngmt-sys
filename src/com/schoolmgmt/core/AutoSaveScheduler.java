package com.schoolmgmt.core;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class AutoSaveScheduler implements AutoCloseable {
    private final Runnable saveAction;
    private final long intervalMillis;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread worker;

    public AutoSaveScheduler(Runnable saveAction, long intervalMillis) {
        if (intervalMillis <= 0) {
            throw new IllegalArgumentException("intervalMillis must be positive");
        }
        this.saveAction = Objects.requireNonNull(saveAction, "saveAction");
        this.intervalMillis = intervalMillis;
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            worker = new Thread(this::loop, "auto-save-thread");
            worker.setDaemon(true);
            worker.start();
        }
    }

    private void loop() {
        while (running.get()) {
            try {
                Thread.sleep(intervalMillis);
                saveAction.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException ex) {
                System.err.println("Auto-save failed: " + ex.getMessage());
            }
        }
    }

    @Override
    public void close() {
        if (running.compareAndSet(true, false) && worker != null) {
            worker.interrupt();
        }
    }
}
