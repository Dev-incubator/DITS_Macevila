package com.example.dits.aspect;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.aspect.loggers.AuthenticationLogger;
import com.example.dits.aspect.loggers.TestPassingLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Component
@Aspect
public class AspectLogger {
    private final AuthenticationLogger authenticationLogger;
    private final TestPassingLogger testPassingLogger;
    @Autowired
    public AspectLogger(AuthenticationLogger authenticationLogger, TestPassingLogger testPassingLogger) {
        this.authenticationLogger = authenticationLogger;
        this.testPassingLogger = testPassingLogger;
    }

    @Pointcut("execution(* com.example.dits.controllers.SecurityController.loginHandle(..))")
    private void authentication() {    }

    @Pointcut("execution(* com.example.dits.controllers.SecurityController.logoutPage(..))")
    private void logout() {    }

    @Pointcut("execution(* com.example.dits.controllers.TestPageController.goTest(..))")
    private void startOfTest() {    }

    @Pointcut("execution(* com.example.dits.controllers.TestPageController.nextTestPage(..))")
    private void proceedingTest() {    }

    @Pointcut("execution(* com.example.dits.controllers.TestPageController.testStatistic(..))")
    private void endOfTest() {    }

    @Around(value = "logout() && args(request, response)", argNames = "proceedingJoinPoint,request,response")
    private Object aroundLogout(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        HttpSession session = request.getSession();
        UserInfoDTO user = (UserInfoDTO) session.getAttribute("user");
        Object retVal = proceedingJoinPoint.proceed();
        authenticationLogger.infoLogOut(new Date(), user);
        return retVal;
    }

    @AfterReturning(value = "proceedingTest() && args(answeredQuestion, model, session)", argNames = "answeredQuestion,model,session")
    private void afterProceedingTest(List<Integer> answeredQuestion,
                                     ModelMap model,
                                     HttpSession session) {
        UserInfoDTO user = (UserInfoDTO) session.getAttribute("user");
        String testName = (String) session.getAttribute("testName");
        String question = (String) model.getAttribute("question");
        testPassingLogger.info(new Date(), user, testName, question, "Proceeding");
    }

    @AfterReturning(value = "endOfTest() && args(answeredQuestion, model, session)", argNames = "answeredQuestion,model,session")
    private void afterEndOfTest(List<Integer> answeredQuestion,
                                ModelMap model,
                                HttpSession session) {
        UserInfoDTO user = (UserInfoDTO) session.getAttribute("user");
        String testName = (String) session.getAttribute("testName");
        testPassingLogger.infoEnd(new Date(), user, testName);
    }

    @AfterReturning(value = "startOfTest() && args(testId, topicName, model, session)", argNames = "testId, topicName,model,session")
    private void afterStartOfTest(int testId, String topicName, ModelMap model, HttpSession session) {
        UserInfoDTO user = (UserInfoDTO) session.getAttribute("user");
        String testName = (String) session.getAttribute("testName");
        String question = (String) model.getAttribute("question");
        testPassingLogger.infoStart(new Date(), user, testName, question);
    }

    @AfterReturning("authentication() && args(session)")
    private void afterAuthentication(HttpSession session) {
        UserInfoDTO user = (UserInfoDTO) session.getAttribute("user");
        authenticationLogger.infoLogIn(new Date(), user);
    }
}
