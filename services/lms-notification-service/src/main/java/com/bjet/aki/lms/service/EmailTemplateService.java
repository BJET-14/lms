package com.bjet.aki.lms.service;

import com.bjet.aki.lms.model.DetailClassSchedule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    @Value("${lms.deployment.host}")
    private String deploymentHost;

    Logger logger = LoggerFactory.getLogger(EmailTemplateService.class);
    private final SpringTemplateEngine templateEngine;

    public String getRegistrationSuccessEmailTemplate(String firstName, String lastName, String email, String password){
        logger.info("Generating a verification email for user.email={}",
                email);
        Context context = new Context();
        String fullName = firstName + " " + lastName;
        context.setVariable("full_name", fullName);
        context.setVariable("email", email);
        context.setVariable("password", password);
        context.setVariable("redirectUrl", deploymentHost);
        return templateEngine.process("user-welcome-email-signup.html", context);
    }

    public String getClassScheduleEmailTemplate(String receiverName, String courseTitle, String teacherName, List<DetailClassSchedule> schedules, String classLink, boolean isSendingToStudent){
        logger.info("Generating a class scheduler email. Receiver name={}", receiverName);
        Context context = new Context();
        context.setVariable("receiverName", receiverName);
        context.setVariable("courseTitle", courseTitle);
        context.setVariable("teacherName", teacherName);
        context.setVariable("schedules", schedules);
        context.setVariable("classLink", classLink);
        if(!isSendingToStudent){
            return templateEngine.process("class-schedule-email.html", context);
        }
        return templateEngine.process("class-schedule-email-student.html", context);
    }

    public String getExamLinkSendEmailTemplate(String examName, String studentName, String examLink, Double fullMark, Double passedMark) {
        logger.info("Generating a exam link sending email for Student. Receiver name={}", studentName);
        Context context = new Context();
        context.setVariable("receiverName", studentName);
        context.setVariable("examName", examName);
        context.setVariable("examLink", examLink);
        context.setVariable("fullMark", fullMark);
        context.setVariable("passedMark", passedMark);
        return templateEngine.process("exam-link-email-student.html", context);
    }
}
