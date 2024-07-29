package com.ilyap.mailservice.service;

import com.ilyap.mailservice.dto.EmailMessage;

public interface MailService {

    void sendMessage(EmailMessage emailMessage);
}
