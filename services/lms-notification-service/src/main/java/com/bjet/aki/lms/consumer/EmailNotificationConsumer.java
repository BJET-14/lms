package com.bjet.aki.lms.consumer;

import com.bjet.aki.lms.model.ClassScheduleSentToEmailRequest;
import com.bjet.aki.lms.model.RegistrationSuccessNotificationRequest;
import com.bjet.aki.lms.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@AllArgsConstructor
@Slf4j
public class EmailNotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "registration-success-topic")
    public void registrationSuccessNotifications(RegistrationSuccessNotificationRequest registrationSuccessNotificationRequest) throws MessagingException {
        log.info(format("Consuming the message from registration-success-topic Topic. Receiver= %s", registrationSuccessNotificationRequest.getEmail()));
        emailService.sendEmail(registrationSuccessNotificationRequest);
    }

    @KafkaListener(topics = "class-schedule-teacher-topic")
    public void classScheduleToTeacherNotifications(ClassScheduleSentToEmailRequest classScheduleSentToEmailRequest) throws MessagingException {
        log.info(format("Consuming the message from class-schedule-teacher-topic Topic. Receiver= %s", classScheduleSentToEmailRequest.getReceiverEmailAddress()));
        classScheduleSentToEmailRequest.setSubject("Class schedule for " + classScheduleSentToEmailRequest.getCourseTitle());
        emailService.sendEmail(classScheduleSentToEmailRequest);
    }

    @KafkaListener(topics = "class-schedule-student-topic")
    public void classScheduleToStudentNotifications(ClassScheduleSentToEmailRequest classScheduleSentToEmailRequest) throws MessagingException {
        log.info(format("Consuming the message from class-schedule-student-topic Topic. Receiver= %s", classScheduleSentToEmailRequest.getReceiverEmailAddress()));
        classScheduleSentToEmailRequest.setSubject("Class schedule for " + classScheduleSentToEmailRequest.getCourseTitle());
        emailService.sendEmail(classScheduleSentToEmailRequest);
    }

}
