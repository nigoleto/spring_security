package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendEmailVerification(String toEmail) {
        String token = jwtUtil.generateEmailVerificationToken(toEmail);
        String verificationUrl = "http://localhost:8080/verify-email?token=" + token;

        String subject = "이메일 인증 요청";
        String body = "아래 링크를 클릭하여 이메일 인증을 완료해주세요:\n" + verificationUrl;

        sendSimpleMail(toEmail, subject, body); // 이메일 발송 로직
    }

}
