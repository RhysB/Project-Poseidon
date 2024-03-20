package com.legacyminecraft.poseidon.util;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.*;

/**
 * The ServerLogRotator class handles the rotation of server logs.
 * It archives the current log file and creates a new one every day at midnight.
 */
public class ServerLogRotator {

    private final ScheduledExecutorService executorService;
    private final String latestLogFileName;

    /**
     * Constructor for the ServerLogRotator class.
     *
     * @param latestLogFileName The name of the latest log file. This file will be archived by the log rotation task.
     */
    public ServerLogRotator(String latestLogFileName) {
        this.executorService = Executors.newScheduledThreadPool(1);
        this.latestLogFileName = latestLogFileName;
    }
    // end of constructor

    /**
     * The archiveLog method is responsible for archiving the current log file.
     * It first checks if the latest log file exists. If it does, it reads its contents,
     * writes the contents to a new file named with the current date, and then clears the latest log file.
     * If the latest log file does not exist, it logs a message and does nothing.
     * If any exception occurs during the process, it logs the exception message.
     */
    private void archiveLog() {
        Logger a = Logger.getLogger("Minecraft");
        try {

            // Check if the latest.log file exists (if it does not, do nothing)
            File latestLog = new File("." + File.separator + "logs" + File.separator + this.latestLogFileName + ".log");
            if (latestLog.exists()) {

                // checks if a log file with the same date already exists (if so, don't overwrite it)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String archivedLogFileName = LocalDateTime.now().format(formatter);
                File archivedLogFile = new File("." + File.separator + "logs" + File.separator + archivedLogFileName + ".log");
                if (archivedLogFile.exists()) { return; }

                // move the latest.log file to a new file with the current date
                String content = new String(Files.readAllBytes(latestLog.toPath())); // read log file contents
                Files.write(archivedLogFile.toPath(), content.getBytes()); // write log file contents to new file
                PrintWriter writer = new PrintWriter(latestLog); // clear the latest log file
                writer.print("");
                writer.close();
            }
        } catch (Exception e) {
            a.log(Level.SEVERE, "Failed to move logs!");
            a.log(Level.SEVERE, e.toString());
        }
        a.log(Level.INFO, "Latest.log has not been created! Skipping log rotation..");
    }
    // end of archiveLog method

    /**
     * The start method is used to start the log rotation task.
     * It calculates the initial delay until the next midnight,
     * and then schedules the log rotation task with this initial delay and a period of 24 hours.
     */
    public void start() {

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        if(now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();
        long period = TimeUnit.DAYS.toSeconds(1);

        // Schedule a task to run every day at midnight
        executorService.scheduleAtFixedRate(this::archiveLog, initialDelay, period, TimeUnit.SECONDS);
        // function can be tested by changing the period to 10 seconds, and the initial delay to 0
        // change the DateTimeFormatter  in archiveLog() to "yyyy-MM-dd-HH-mm-ss"
    }
    // end of start method

}
// end of ServerLogRotator.java

