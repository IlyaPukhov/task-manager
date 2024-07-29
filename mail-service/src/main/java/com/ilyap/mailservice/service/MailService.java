package com.ilyap.mailservice.service;

import com.ilyap.mailservice.dto.VerificationEmailMessage;

public interface MailService {

    void sendMessage(VerificationEmailMessage emailMessage);
}
