package com.jobon.social.vo;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * SOCIAL_ACCOUNT 테이블 데이터를 담는 VO입니다.
 * JOBON 회원과 Google/Naver/Kakao 계정의 연결 정보를 저장합니다.
 */
import java.time.LocalDateTime;

public class SocialAccountVO {
    private Long socialAccountId;
    private Long memberId;
    private String provider;
    private String providerUserId;
    private String email;
    private LocalDateTime linkedAt;
    private LocalDateTime lastLoginAt;

    public Long getSocialAccountId() {
        return socialAccountId;
    }

    public void setSocialAccountId(Long socialAccountId) {
        this.socialAccountId = socialAccountId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(LocalDateTime linkedAt) {
        this.linkedAt = linkedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
