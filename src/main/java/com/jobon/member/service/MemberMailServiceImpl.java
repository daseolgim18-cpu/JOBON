package com.jobon.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * [추가] Gmail SMTP 기반 임시 비밀번호 발송 구현체입니다.
 * MAIL_USERNAME / MAIL_APP_PASSWORD 환경변수를 사용하며 비밀번호를 코드에 저장하지 않습니다.
 */
@Service
public class MemberMailServiceImpl implements MemberMailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String from;

    public MemberMailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendTemporaryPassword(String to, String loginId, String temporaryPassword) {
        if (from == null || from.isBlank()) {
            throw new IllegalStateException("메일 발송 계정이 설정되지 않았습니다. MAIL_USERNAME 환경변수를 확인해주세요.");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("[JOBON] 임시 비밀번호 안내");
        message.setText("JOBON 비밀번호 찾기 요청으로 임시 비밀번호를 발급했습니다.\n\n"
                + "아이디: " + loginId + "\n"
                + "임시 비밀번호: " + temporaryPassword + "\n\n"
                + "로그인 후 마이페이지 > 비밀번호 변경에서 새 비밀번호로 변경해주세요.");
        mailSender.send(message);
    }
}
