package com.jobon.social.vo;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 각 SNS에서 받은 사용자 정보를 공통 형식으로 변환한 불변 데이터 객체(record)입니다.
 * Google/Naver/Kakao 응답 구조 차이를 이후 로직에서 신경 쓰지 않도록 통일합니다.
 */
public record SocialProfile(String provider, String providerUserId, String email, String name, String nickname, String profileImageUrl) {}
