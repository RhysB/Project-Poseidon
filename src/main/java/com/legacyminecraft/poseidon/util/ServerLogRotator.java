package com.legacyminecraft.poseidon.util;

import org.bukkit.Bukkit;
import com.legacyminecraft.poseidon.PoseidonPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
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

    /**
     * Checks if the date in the log line is today's date
     * @param date The date in the log line. Format: "yyyy-MM-dd"
     * @return True if the date in the log line is today's date, false otherwise
     */
    private boolean isToday(String date) {
    String[] dateParts = date.split("-");
        LocalDateTime logLineDateTime = LocalDateTime.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]), 0, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        return logLineDateTime.getYear() == now.getYear() && logLineDateTime.getMonthValue() == now.getMonthValue() && logLineDateTime.getDayOfMonth() == now.getDayOfMonth();
    }

    /**
     * Archives a log line to a log file with the same date as the date in the log line
     * @param parts The log line to archive to a log file haven been split already e.g. ["2024-03-20", "13:02:27", "[INFO]", "This is a log message..."]
     */
    private void archiveLine(String[] parts) {

        try {

            String date = parts[0];
            String time = parts[1];
            String logLevel = parts[2];
            String message = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
            // check if a log file with this information already exists
            File logFile = new File("." + File.separator + "logs" + File.separator + date + ".log");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            // append the log line to the log file with the same date as the date in the log line
            FileWriter fileWriter = new FileWriter(logFile, true);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.println(date + " " + time + " " + logLevel + " " + message);
            writer.close();

        // catch any exceptions that occur during the process, and log them. IOExceptions are possible when calling createNewFile()
        } catch (IOException e) {
            logger.log(Level.SEVERE, "[Poseidon]  Failed to create new log file!");
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Builds historical logs from the latest.log file. Logs from today's date are kept in the latest.log file, while logs from previous dates are archived to log files with the same date as the date in the log line.
     * Note that if latest.log contains logs from multiple days, the logs will be split by date and archived to the appropriate log files.
     */
    private void buildHistoricalLogsFromLatestLogFile() {

        logger.log(Level.INFO, "[Poseidon] Building logs from latest.log...");

        try {
            // open latest log file
            File latestLog = new File("." + File.separator + "logs" + File.separator + this.latestLogFileName + ".log");
            if (!latestLog.exists()) {
                logger.log(Level.INFO, "[Poseidon] No logs to build from latest.log!");
                return;
            }

            // split the contents of the latest log file by line (and strip the newline character)
            String content = new String(Files.readAllBytes(latestLog.toPath()));
            String[] lines = content.split("\n");
            // create a StringBuilder to store today's logs (to write back to latest.log after archiving the rest of the logs)
            StringBuilder todayLogs = new StringBuilder();

            for (String line : lines) {

                String[] splitLine = line.split(" ");
                if (splitLine.length < 3) { // all lines will start with a date, time, and log level e.g. "2024-03-20 13:02:27 [INFO]"
                    continue;
                }

                // make sure the first index is a date
                if (!splitLine[0].matches("\\d{4}-\\d{2}-\\d{2}")) {
                    continue;
                }

                // if the log line is of today's date, do not archive it (ignore times)
                if (isToday(splitLine[0])) {
                    todayLogs.append(line).append("\n");
                    continue;
                }

                // archive the log line to a log file with the same date as the date in the log line
                archiveLine(splitLine);

            }

            // clear latest.log and write back today's logs from the StringBuilder
            FileWriter fileWriter = new FileWriter(latestLog);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.print(todayLogs);
            writer.close();

            logger.log(Level.INFO, "[Poseidon] Logs built from latest.log!");

        // catch any exceptions that occur during the process, and log them
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[Poseidon] Failed to build logs from latest.log!");
            logger.log(Level.SEVERE, e.toString());
        }
    }

    public void start() {
        // Calculate the initial delay and period for the log rotation task
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        if(now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);
        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();
        long period = TimeUnit.DAYS.toSeconds(1);

        // do log rotation immediately upon startup to ensure that logs are archived correctly.
        buildHistoricalLogsFromLatestLogFile();

        // Schedule the log rotation task to run every day at midnight offset by one second to avoid missing logs
        logger.log(Level.INFO, "[Poseidon] Log rotation task scheduled for run in " + initialDelay + " seconds, and then every " + period + " seconds.");
        logger.log(Level.INFO, "[Poseidon] If latest.log contains logs from earlier, not previously archived dates, they will be archived to the appropriate log files " +
                               "upon first run of the log rotation task. If log files already exist for these dates, the logs will be appended to the existing log files!");
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(new PoseidonPlugin(), this::buildHistoricalLogsFromLatestLogFile, (initialDelay + 1) * 20, period * 20);
    }
}

