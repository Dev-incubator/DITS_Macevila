package com.example.dits.aspect.loggers;

import com.example.dits.aspect.loggers.appenders.FileAppender;
import com.example.dits.dto.UserInfoDTO;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AuthenticationLogger {
    private static final String LOGIN_MESSAGE = "Log in";
    private static final String LOGOUT_MESSAGE = "Log out";
    private static final FileAppender fileAppender = new FileAppender("src/main/resources/authentication.log", "%25s : %10s %10s %10s %10s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

    public void info(Date date, UserInfoDTO user, String message) {
        fileAppender.write(date, message, user.getLogin(), user.getFirstName(), user.getLastName(), user.getRole());
    }

    public void infoLogIn(Date date, UserInfoDTO user) {
        info(date, user, LOGIN_MESSAGE);
    }

    public void infoLogOut(Date date, UserInfoDTO user) {
        info(date, user, LOGOUT_MESSAGE);
    }
}