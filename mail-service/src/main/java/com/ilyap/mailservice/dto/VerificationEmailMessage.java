package com.ilyap.mailservice.dto;

public record VerificationEmailMessage(String email,
                                       String verificationUrl) {

}
