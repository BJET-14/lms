package com.bjet.aki.lms.service;

import com.bjet.aki.lms.domain.RegistrationSuccessNotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    @Async
    public void sendEmail(RegistrationSuccessNotificationRequest request) throws MessagingException {
        log.info("Sending email. to={} subject={}", request.getEmail(), request.getSubject());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("bjet.aki.info@gmail.com");
        helper.setTo(request.getEmail());
        helper.setSubject(request.getSubject());
        try {
            String htmlBody = templateService.getRegistrationSuccessEmailTemplate(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Email sent.");
        } catch (MessagingException e) {
            log.error("Could not send email.", e);
            String err = String.format("There was an error sending an email to %s." +
                    "Please verify the email is valid and try again.", request.getEmail());
            throw new RuntimeException(err);
        }
    }
}
