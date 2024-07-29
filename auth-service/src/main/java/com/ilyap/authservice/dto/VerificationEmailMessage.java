package com.ilyap.authservice.dto;

public record VerificationEmailMessage(String email,
                                       String verificationUrl) {

}
