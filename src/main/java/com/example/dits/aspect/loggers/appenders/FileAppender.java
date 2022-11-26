package com.example.dits.aspect.loggers.appenders;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;

public class FileAppender {
    private final String fileName;
    private final String pattern;
    private final DateFormat dateFormat;

    public FileAppender(String fileName, String pattern, DateFormat dateFormat) {
        this.fileName = fileName;
        this.pattern = pattern;
        this.dateFormat = dateFormat;
    }

    public void write(Date date, Object... messages) {
        try (Writer writer = new FileWriter(fileName, true)) {
            String event = dateFormat.format(date) + " " + String.format(pattern, messages) + "\n";
            writer.write(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}