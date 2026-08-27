package com.jobon.member.service;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 회원 비즈니스 로직 구현체입니다.
 * 회원가입 중복검사, BCrypt 암호화, 일반 로그인 검증, SNS 신규회원 생성 등을 처리합니다.
 */
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobon.member.dao.MemberDAO;
import com.jobon.member.dto.JoinRequest;
import com.jobon.member.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberDAO memberDAO;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberDAO memberDAO, PasswordEncoder passwordEncoder) {
        this.memberDAO = memberDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    // DB에 동일한 LOGIN_ID가 없을 때만 true를 반환한다.
    public boolean isLoginIdAvailable(String loginId) {
        return loginId != null && !loginId.isBlank() && memberDAO.countByLoginId(loginId.trim()) == 0;
    }

    @Override
    // 이메일은 소문자로 정규화한 뒤 DB 중복 여부를 확인한다.
    public boolean isEmailAvailable(String email) {
        return email != null && !email.isBlank() && memberDAO.countByEmail(email.trim().toLowerCase()) == 0;
    }

    @Override
    @Transactional
    public MemberVO join(JoinRequest request) {
        if (!request.isPasswordMatched()) throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        if (!isLoginIdAvailable(request.getLoginId())) throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        if (!isEmailAvailable(request.getEmail())) throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        MemberVO member = new MemberVO();
        member.setLoginId(request.getLoginId().trim());
        member.setEmail(request.getEmail().trim().toLowerCase());
        member.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        member.setName(request.getName().trim());
        member.setNickname(request.getNickname().trim());
        member.setPhone(normalizePhone(request.getPhone()));
        member.setInterestJob(blankToNull(request.getInterestJob()));
        member.setCareerType(blankToNull(request.getCareerType()));
        member.setEducationLevel(blankToNull(request.getEducationLevel()));
        member.setSchoolName(blankToNull(request.getSchoolName()));
        member.setMajorName(blankToNull(request.getMajorName()));
        member.setPreferredLocation(blankToNull(request.getPreferredLocation()));
        member.setTermsAgreedYn("Y");
        member.setPrivacyAgreedYn("Y");
        member.setStatus("ACTIVE");
        memberDAO.insertMember(member);
        return member;
    }

    @Override
    @Transactional
    // 일반 로그인 처리: 회원 존재/상태를 확인하고 BCrypt matches()로 비밀번호를 검증한다.
    public MemberVO login(String loginId, String rawPassword) {
        MemberVO member = memberDAO.selectByLoginId(loginId == null ? null : loginId.trim());
        if (member == null || member.getPasswordHash() == null || !passwordEncoder.matches(rawPassword, member.getPasswordHash())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (!"ACTIVE".equalsIgnoreCase(member.getStatus())) {
            throw new IllegalStateException("현재 로그인할 수 없는 계정입니다.");
        }
        memberDAO.updateLastLoginAt(member.getMemberId());
        return memberDAO.selectByMemberId(member.getMemberId());
    }

    @Override public MemberVO findById(Long memberId) { return memberDAO.selectByMemberId(memberId); }
    @Override public MemberVO findByEmail(String email) { return email == null ? null : memberDAO.selectByEmail(email.trim().toLowerCase()); }

    @Override
    @Transactional
    // SNS 최초 로그인 사용자에게 대응되는 JOBON_MEMBER 계정을 생성한다.
    public MemberVO createSocialMember(String provider, String providerUserId, String email, String name, String nickname, String profileImageUrl) {
        MemberVO member = new MemberVO();
        String safeProvider = provider.toLowerCase();
        String baseLoginId = safeProvider + "_" + providerUserId.replaceAll("[^a-zA-Z0-9]", "");
        if (baseLoginId.length() > 45) baseLoginId = baseLoginId.substring(0, 45);
        String loginId = baseLoginId;
        int suffix = 1;
        while (memberDAO.countByLoginId(loginId) > 0) loginId = baseLoginId + "_" + suffix++;

        String resolvedEmail = blankToNull(email);
        if (resolvedEmail != null) {
            MemberVO byEmail = memberDAO.selectByEmail(resolvedEmail.toLowerCase());
            if (byEmail != null) return byEmail;
        }

        String resolvedName = blankToNull(name);
        if (resolvedName == null) resolvedName = provider + " 회원";
        String resolvedNickname = blankToNull(nickname);
        if (resolvedNickname == null) resolvedNickname = resolvedName;

        member.setLoginId(loginId);
        member.setEmail(resolvedEmail == null ? loginId + "@social.jobon.local" : resolvedEmail.toLowerCase());
        member.setPasswordHash(null);
        member.setName(resolvedName);
        member.setNickname(resolvedNickname);
        member.setProfileImageUrl(blankToNull(profileImageUrl));
        member.setTermsAgreedYn("Y");
        member.setPrivacyAgreedYn("Y");
        member.setStatus("ACTIVE");
        memberDAO.insertMember(member);
        return member;
    }

    @Override public void touchLastLogin(Long memberId) { memberDAO.updateLastLoginAt(memberId); }

    private String normalizePhone(String phone) { return phone == null ? null : phone.replaceAll("[^0-9]", ""); }
    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
