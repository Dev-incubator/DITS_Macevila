package com.example.dits.aspect.loggers;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.aspect.loggers.appenders.FileAppender;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TestPassingLogger {
    private static final String START_TEST_MESSAGE = "Test started";
    private static final String END_TEST_MESSAGE = "Test ended";
    private static final FileAppender fileAppender = new FileAppender("src/main/resources/userTesting.log", "%25s : %10s %10s %40s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

    public void info(Date date, UserInfoDTO user, String testName, String question, String message) {
        fileAppender.write(date, message, user.getLogin(), testName, question);
    }

    public void infoStart(Date date, UserInfoDTO user, String testName, String question) {
        info(date, user, testName, question, START_TEST_MESSAGE);
    }

    public void infoEnd(Date date, UserInfoDTO user, String testName) {
        info(date, user, testName, "", END_TEST_MESSAGE);
    }
}