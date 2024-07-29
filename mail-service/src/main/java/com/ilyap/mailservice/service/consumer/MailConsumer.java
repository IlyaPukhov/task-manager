package com.ilyap.mailservice.service.consumer;

import com.ilyap.logging.annotation.Logged;
import com.ilyap.mailservice.dto.VerificationEmailMessage;
import com.ilyap.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Logged
@RequiredArgsConstructor
public class MailConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "email-sending-queue", groupId = "mail-service")
    public void consume(VerificationEmailMessage emailMessage, Acknowledgment acknowledgment) {
        mailService.sendMessage(emailMessage);
        acknowledgment.acknowledge();
    }

}
