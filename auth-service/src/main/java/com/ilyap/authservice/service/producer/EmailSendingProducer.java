package com.ilyap.authservice.service.producer;

import com.ilyap.authservice.dto.VerificationEmailMessage;
import com.ilyap.logging.annotation.Logged;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Logged
@RequiredArgsConstructor
public class EmailSendingProducer {

    private final KafkaTemplate<String, VerificationEmailMessage> kafkaTemplate;

    public void sendEmail(VerificationEmailMessage emailMessage) {
        kafkaTemplate.send("email-sending-queue", emailMessage);
    }
}
