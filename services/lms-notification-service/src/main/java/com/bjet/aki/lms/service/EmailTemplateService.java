package com.bjet.aki.lms.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    @Value("${lms.deployment.environment}")
    private String deploymentEnvironment;

    Logger logger = LoggerFactory.getLogger(EmailTemplateService.class);
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";
    private final SpringTemplateEngine templateEngine;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public String generateRegistrationEmail(String firstName, String lastName, String email, String emailConfirmationUrl){
        logger.info("Generating a verification email for user.email={}",
                email);
        return null;
    }


}
