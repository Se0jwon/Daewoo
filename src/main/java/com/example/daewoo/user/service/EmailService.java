package com.example.daewoo.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[Daewoo] 이메일 인증번호 안내");
        message.setText("안녕하세요. 이메일 인증을 위한 6자리 인증번호는 다음과 같습니다.\n" + code);
        try {
            mailSender.send(message);
            log.info("인증번호 이메일 전송 완료: {}", to);
        } catch (Exception e) {
            log.error("인증번호 이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }
    }
}