package com.bjet.aki.lms.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

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
}
