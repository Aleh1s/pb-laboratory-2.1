package org.example;

import org.example.exception.CanNotCreateLogFileException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    private static Logger logger;
    private static final String logPath = Property
            .getInstance()
            .getProperties()
            .getProperty("default.logger.path");
    private static final String FILE_NAME = "logs.txt";
    private Logger() {}

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }

        if (!loggerFileExists()) {
            try {
                createNewLoggerFile();
            } catch (CanNotCreateLogFileException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }

        return logger;
    }

    public static String getLogFilePath() {
        return new File(logPath, FILE_NAME).getPath();
    }

    public void log(String message) {
        try (FileWriter writer = new FileWriter(new File(logPath, FILE_NAME), true)) {
            writer.write(String.format("[%s] %s\n", LocalDateTime.now(), message));
        } catch (IOException e) {
            System.err.println("Can not write to " + logPath + "/" + FILE_NAME);
        }
    }

    private static boolean loggerFileExists() {
        return new File(logPath, FILE_NAME).exists();
    }

    private static boolean createNewLoggerFile() throws CanNotCreateLogFileException {
        boolean created = false;
        File file = new File(logPath, FILE_NAME);
        if (!file.exists()) {
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                throw new CanNotCreateLogFileException("Can not create log file in " + logPath, e);
            }
        }

        return created;
    }
}
