package com.example.member.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);           // 수신자 이메일
        message.setSubject(subject); // 이메일 제목
        message.setText(content);    // 이메일 내용
        mailSender.send(message);    // 이메일 전송
    }
}
