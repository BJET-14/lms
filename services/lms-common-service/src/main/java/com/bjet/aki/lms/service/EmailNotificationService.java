package com.bjet.aki.lms.service;

import com.bjet.aki.lms.domain.RegistrationSuccessNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    private final KafkaTemplate<String, RegistrationSuccessNotificationRequest> kafkaTemplate;

    public void sendEmailNotificationForRegistration(RegistrationSuccessNotificationRequest registrationSuccessNotificationRequest){
        logger.info("Sending notification with body = < {} >", registrationSuccessNotificationRequest);
        Message<RegistrationSuccessNotificationRequest> message = MessageBuilder
                .withPayload(registrationSuccessNotificationRequest)
                .setHeader(TOPIC, "registration-success-topic")
                .build();
        try {
            kafkaTemplate.send(message);
        } catch (Exception e){
            logger.info("Exception occurred in sending notification with body = < {} >", registrationSuccessNotificationRequest);
        }
    }
}
