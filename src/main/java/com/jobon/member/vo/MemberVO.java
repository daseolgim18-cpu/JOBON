package com.jobon.member.vo;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * JOBON_MEMBER 테이블 데이터를 담는 VO입니다.
 * DB 컬럼과 Java 객체 사이에서 회원 정보를 전달하는 용도로 사용합니다.
 */
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class MemberVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private String loginId;
    private String email;
    private String passwordHash;
    private String name;
    private String nickname;
    private String phone;
    private String interestJob;
    private String careerType;
    private String educationLevel;
    private String schoolName;
    private String majorName;
    private String preferredLocation;
    private String introduction;
    private String profileImageUrl;
    private String termsAgreedYn;
    private String privacyAgreedYn;
    private String status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getInterestJob() { return interestJob; }
    public void setInterestJob(String interestJob) { this.interestJob = interestJob; }
    public String getCareerType() { return careerType; }
    public void setCareerType(String careerType) { this.careerType = careerType; }
    public String getEducationLevel() { return educationLevel; }
    public void setEducationLevel(String educationLevel) { this.educationLevel = educationLevel; }
    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
    public String getMajorName() { return majorName; }
    public void setMajorName(String majorName) { this.majorName = majorName; }
    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public String getTermsAgreedYn() { return termsAgreedYn; }
    public void setTermsAgreedYn(String termsAgreedYn) { this.termsAgreedYn = termsAgreedYn; }
    public String getPrivacyAgreedYn() { return privacyAgreedYn; }
    public void setPrivacyAgreedYn(String privacyAgreedYn) { this.privacyAgreedYn = privacyAgreedYn; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
