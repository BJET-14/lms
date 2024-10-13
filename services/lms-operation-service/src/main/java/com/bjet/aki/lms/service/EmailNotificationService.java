package com.bjet.aki.lms.service;

import com.bjet.aki.lms.model.ClassScheduleSentToEmailRequest;
import com.bjet.aki.lms.model.ExamLinkSentToStudentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final KafkaTemplate<String, ?> kafkaTemplate;

    public void sendEmailWithClassScheduleToTeacher(ClassScheduleSentToEmailRequest classScheduleSentToEmailRequest){
        log.info("Sending class schedule to teacher with body = < {} >", classScheduleSentToEmailRequest);
        Message<ClassScheduleSentToEmailRequest> message = MessageBuilder
                .withPayload(classScheduleSentToEmailRequest)
                .setHeader(TOPIC, "class-schedule-teacher-topic")
                .build();
        try {
            kafkaTemplate.send(message);
        } catch (Exception e){
            log.error("Exception occurred in sending class schedule to teacher with body = < {} >", classScheduleSentToEmailRequest);
        }
    }

    public void sendEmailWithClassScheduleToStudent(ClassScheduleSentToEmailRequest classScheduleSentToEmailRequest){
        log.info("Sending class schedule to student with body = < {} >", classScheduleSentToEmailRequest);
        Message<ClassScheduleSentToEmailRequest> message = MessageBuilder
                .withPayload(classScheduleSentToEmailRequest)
                .setHeader(TOPIC, "class-schedule-student-topic")
                .build();
        try {
            kafkaTemplate.send(message);
        } catch (Exception e){
            log.error("Exception occurred in sending class schedule to student with body = < {} >", classScheduleSentToEmailRequest);
        }
    }


    public void sendEmailWithExamLinkToStudent(ExamLinkSentToStudentRequest emailRequest) {
        log.info("Sending exam link to student with body = < {} >", emailRequest);
        Message<ExamLinkSentToStudentRequest> message = MessageBuilder
                .withPayload(emailRequest)
                .setHeader(TOPIC, "exam-link-student-topic")
                .build();
        try {
            kafkaTemplate.send(message);
        } catch (Exception e){
            log.error("Exception occurred in sending exam link to student with body = < {} >", emailRequest);
        }
    }
}
