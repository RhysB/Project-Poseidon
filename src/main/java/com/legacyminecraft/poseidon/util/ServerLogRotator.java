package com.legacyminecraft.poseidon.util;

import org.bukkit.Bukkit;
import com.legacyminecraft.poseidon.PoseidonPlugin;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.*;

public class ServerLogRotator {
    private final String latestLogFileName;
    private final Logger logger;

    public ServerLogRotator(String latestLogFileName) {
        this.latestLogFileName = latestLogFileName;
        this.logger = Logger.getLogger("Minecraft");
    }

    private void archiveLog() {
        try {
            logger.log(Level.INFO, "Archiving contents of latest.log to a new file!");
            // Check if the latest.log file exists (if it does not, do nothing)
            File latestLog = new File("." + File.separator + "logs" + File.separator + this.latestLogFileName + ".log");
            if (latestLog.exists()) {
                // checks if a log file with the same date already exists (if so, don't overwrite it)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String archivedLogFileName = LocalDateTime.now().minusDays(1).format(formatter);
                File archivedLogFile = new File("." + File.separator + "logs" + File.separator + archivedLogFileName + ".log");
                if (archivedLogFile.exists()) { return; }
                // move the latest.log file to a new file with the current date
                String content = new String(Files.readAllBytes(latestLog.toPath()));
                Files.write(archivedLogFile.toPath(), content.getBytes());
                PrintWriter writer = new PrintWriter(latestLog);
                writer.print("");
                writer.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to move logs!");
            logger.log(Level.SEVERE, e.toString());
        }
    }

    public void start() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        if(now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);
        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();
        long period = TimeUnit.DAYS.toSeconds(1);
        // Schedule the log rotation task
        logger.log(Level.INFO, "Log rotation task scheduled for first run in " + initialDelay + " seconds, and then every " + period + " seconds.");
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(new PoseidonPlugin(), this::archiveLog, initialDelay * 20, period * 20);
    }
}

