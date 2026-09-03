package com.jobon.member.service;

/** [추가] 회원 비밀번호 찾기 메일 발송 서비스 */
public interface MemberMailService {
    void sendTemporaryPassword(String to, String loginId, String temporaryPassword);
}
