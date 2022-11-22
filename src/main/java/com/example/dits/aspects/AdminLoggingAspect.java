package com.example.dits.aspects;

import com.example.dits.dto.TestInfoDTO;
import com.example.dits.dto.TopicDTO;
import com.example.dits.dto.UserInfoDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
public class AdminLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(AdminLoggingAspect.class);

    @After("com.example.dits.aspects.Pointcuts.addUser()")
    public void loggingAfterAddUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        UserInfoDTO user = (UserInfoDTO) args[0];
        String message = "New user (" + user.getLogin() + ") is created, time: " + LocalDateTime.now();
        logger.info(message);
    }

    @After("com.example.dits.aspects.Pointcuts.addTopic()")
    public void loggingAfterAddTopic(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        TopicDTO topic = (TopicDTO) args[0];
        String message = "New topic (" + topic.getTopicName() + ") is created, time: " + LocalDateTime.now();
        logger.info(message);
    }

    @After("com.example.dits.aspects.Pointcuts.addTest()")
    public void loggingAfterAddTest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        TestInfoDTO test = (TestInfoDTO) args[0];
        String message = "New test (" + test.getName() + ") was created, time: " + LocalDateTime.now();
        logger.info(message);
    }
}
