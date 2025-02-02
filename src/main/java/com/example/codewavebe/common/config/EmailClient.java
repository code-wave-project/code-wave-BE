package com.example.codewavebe.common.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailClient {

    private final JavaMailSender javaMailSender;
    private final JavaMailSenderImpl mailSender;

    @Transactional // 트랜잭션 격리 - 롤백용
    public void sendOneEmail(String from, String to, String joinCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 수신자, 제목, 본문 설정
            String subject = from + "님.";
            String body =
                    "<br> 닉네임 변경 코드: " + joinCode;
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            // 이메일 전송
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
