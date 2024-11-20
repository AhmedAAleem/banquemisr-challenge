package com.banquemisr.challenge05.service;

public interface EmailService {
    void sendEmail(String to, String subject, String textBody);
}
