package com.ilyap.mailservice.service.impl;

import com.ilyap.logging.annotation.Logged;
import com.ilyap.mailservice.dto.VerificationEmailMessage;
import com.ilyap.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Logged
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.sender.email}")
    private String sender;

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    @Override
    @SneakyThrows
    public void sendMessage(VerificationEmailMessage emailMessage) {
        var message = emailSender.createMimeMessage();

        var helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                UTF_8.name()
        );

        Context context = new Context();
        context.setVariable("verificationUrl", emailMessage.verificationUrl());
        String emailContent = templateEngine.process("verify-email", context);

        helper.setTo(emailMessage.email());
        helper.setSubject("Task Manager Verification");
        helper.setFrom(sender);
        helper.setText(emailContent, true);

        emailSender.send(message);
    }
}
