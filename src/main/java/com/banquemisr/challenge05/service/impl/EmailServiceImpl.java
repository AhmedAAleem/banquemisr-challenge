package com.banquemisr.challenge05.service.impl;

import com.banquemisr.challenge05.exception.email.EmailServiceException;
import com.banquemisr.challenge05.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String textBody) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("tbankmisr@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(textBody);

            log.info("Preparing to send plain text email to: {}", to);
            mailSender.send(message);
            log.info("Plain text email sent successfully to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new EmailServiceException("Failed to send email to " + to, e);
        }
    }
}