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
    private final MemberMailService memberMailService;

    public MemberServiceImpl(MemberDAO memberDAO, PasswordEncoder passwordEncoder,
            MemberMailService memberMailService) {
        this.memberDAO = memberDAO;
        this.passwordEncoder = passwordEncoder;
        this.memberMailService = memberMailService;
    }

    @Override
    // DB에 동일한 LOGIN_ID가 없을 때만 true를 반환한다.
    public boolean isLoginIdAvailable(String loginId) {
        return loginId != null && !loginId.isBlank() && memberDAO.countByLoginId(loginId.trim()) == 0;
    }

    @Override
    // 이메일은 소문자로 정규화한 뒤 DB 중복 여부를 확인한다.
    public boolean isEmailAvailable(String email) {
        return isValidEmail(email) && memberDAO.countByEmail(email.trim().toLowerCase()) == 0;
    }

    @Override
    public boolean isEmailAvailable(String email, Long memberId) {
        return memberId != null && isValidEmail(email)
                && memberDAO.countByEmailExcludingMember(email.trim().toLowerCase(), memberId) == 0;
    }

    // [수정] 현재 로그인 회원을 제외하고 같은 닉네임이 존재하지 않을 때 true를 반환한다.
    @Override
    public boolean isNicknameAvailable(String nickname, Long memberId) {
        return nickname != null
                && !nickname.isBlank()
                && memberId != null
                && memberDAO.countByNicknameExcludingMember(nickname.trim(), memberId) == 0;
    }

    @Override
    @Transactional
    public MemberVO join(JoinRequest request) {
        if (!request.isPasswordMatched())
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        if (!isLoginIdAvailable(request.getLoginId()))
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        if (!isEmailAvailable(request.getEmail()))
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

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
        if (member == null || member.getPasswordHash() == null
                || !passwordEncoder.matches(rawPassword, member.getPasswordHash())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (!"ACTIVE".equalsIgnoreCase(member.getStatus())) {
            throw new IllegalStateException("현재 로그인할 수 없는 계정입니다.");
        }
        memberDAO.updateLastLoginAt(member.getMemberId());
        return memberDAO.selectByMemberId(member.getMemberId());
    }

    @Override
    public MemberVO findById(Long memberId) {
        return memberDAO.selectByMemberId(memberId);
    }

    @Override
    public MemberVO findByEmail(String email) {
        return email == null ? null : memberDAO.selectByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional
    // [수정] 마이페이지 프로필 수정값을 정리한 뒤 DB를 갱신하고 최신 회원 정보를 다시 조회한다.
    public MemberVO updateProfile(MemberVO member) {
        if (member == null || member.getMemberId() == null) {
            throw new IllegalArgumentException("회원 정보가 올바르지 않습니다.");
        }

        String email = blankToNull(member.getEmail());
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("올바른 이메일 형식을 입력해주세요.");
        }
        email = email.trim().toLowerCase();
        if (!isEmailAvailable(email, member.getMemberId())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String nickname = blankToNull(member.getNickname());
        if (nickname == null) {
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        }
        if (nickname.length() > 50) {
            throw new IllegalArgumentException("닉네임은 50자 이하로 입력해주세요.");
        }

        // [수정] 화면의 중복 확인 여부와 관계없이 저장 직전에 서버에서도 닉네임 중복을 다시 검사한다.
        if (!isNicknameAvailable(nickname, member.getMemberId())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        String introduction = blankToNull(member.getIntroduction());
        if (introduction != null && introduction.length() > 300) {
            throw new IllegalArgumentException("한 줄 소개는 300자 이하로 입력해주세요.");
        }

        String interestJob = blankToNull(member.getInterestJob());
        String preferredLocation = blankToNull(member.getPreferredLocation());
        if (interestJob != null && interestJob.length() > 100) {
            throw new IllegalArgumentException("관심 직무는 100자 이하로 입력해주세요.");
        }
        if (preferredLocation != null && preferredLocation.length() > 100) {
            throw new IllegalArgumentException("희망 근무지는 100자 이하로 입력해주세요.");
        }

        member.setEmail(email);
        member.setNickname(nickname);
        member.setIntroduction(introduction);
        member.setInterestJob(interestJob);
        member.setPreferredLocation(preferredLocation);
        member.setProfileImageUrl(blankToNull(member.getProfileImageUrl()));

        int updated = memberDAO.updateProfile(member);
        if (updated != 1) {
            throw new IllegalStateException("프로필을 수정하지 못했습니다.");
        }
        return memberDAO.selectByMemberId(member.getMemberId());
    }

    @Override
    public String findLoginId(String name, String email, String phone) {
        String safeName = blankToNull(name);
        String safeEmail = blankToNull(email);
        String safePhone = normalizePhone(phone);
        if (safeName == null || !isValidEmail(safeEmail) || safePhone == null) {
            throw new IllegalArgumentException("이름, 이메일, 휴대폰 번호를 정확히 입력해주세요.");
        }
        MemberVO member = memberDAO.selectForFindId(safeName, safeEmail.toLowerCase(), safePhone);
        if (member == null)
            throw new IllegalArgumentException("일치하는 회원 정보를 찾을 수 없습니다.");
        return member.getLoginId();
    }

    @Override
    @Transactional
    public void issueTemporaryPassword(String loginId, String name, String email) {
        String safeLoginId = blankToNull(loginId);
        String safeName = blankToNull(name);
        String safeEmail = blankToNull(email);
        if (safeLoginId == null || safeName == null || !isValidEmail(safeEmail)) {
            throw new IllegalArgumentException("아이디, 이름, 이메일을 정확히 입력해주세요.");
        }
        safeEmail = safeEmail.toLowerCase();
        MemberVO member = memberDAO.selectForPasswordReset(safeLoginId, safeName, safeEmail);
        if (member == null)
            throw new IllegalArgumentException("일치하는 회원 정보를 찾을 수 없습니다.");
        if (member.getPasswordHash() == null || member.getPasswordHash().isBlank()) {
            throw new IllegalStateException("소셜 로그인 전용 계정은 해당 SNS로 로그인해주세요.");
        }

        String temporaryPassword = createTemporaryPassword();
        // 같은 트랜잭션에서 비밀번호를 갱신하고, 메일 발송이 실패하면 예외로 트랜잭션을 롤백합니다.
        if (memberDAO.updatePassword(member.getMemberId(), passwordEncoder.encode(temporaryPassword)) != 1) {
            throw new IllegalStateException("임시 비밀번호 저장에 실패했습니다.");
        }
        memberMailService.sendTemporaryPassword(member.getEmail(), member.getLoginId(), temporaryPassword);
    }

    @Override
    @Transactional
    // SNS 최초 로그인 사용자에게 대응되는 JOBON_MEMBER 계정을 생성한다.
    public MemberVO createSocialMember(String provider, String providerUserId, String email, String name,
            String nickname, String profileImageUrl) {
        MemberVO member = new MemberVO();
        String safeProvider = provider.toLowerCase();
        String baseLoginId = safeProvider + "_" + providerUserId.replaceAll("[^a-zA-Z0-9]", "");
        if (baseLoginId.length() > 45)
            baseLoginId = baseLoginId.substring(0, 45);
        String loginId = baseLoginId;
        int suffix = 1;
        while (memberDAO.countByLoginId(loginId) > 0)
            loginId = baseLoginId + "_" + suffix++;

        String resolvedEmail = blankToNull(email);
        if (resolvedEmail != null) {
            MemberVO byEmail = memberDAO.selectByEmail(resolvedEmail.toLowerCase());
            if (byEmail != null)
                return byEmail;
        }

        String resolvedName = blankToNull(name);
        if (resolvedName == null)
            resolvedName = provider + " 회원";
        String resolvedNickname = blankToNull(nickname);
        if (resolvedNickname == null)
            resolvedNickname = resolvedName;

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

    @Override
    public void touchLastLogin(Long memberId) {
        memberDAO.updateLastLoginAt(memberId);
    }

    @Override
    public boolean hasLocalPassword(Long memberId) {
        MemberVO member = findById(memberId);
        return member != null && member.getPasswordHash() != null && !member.getPasswordHash().isBlank();
    }

    @Override
    @Transactional
    public void changePassword(Long memberId, String currentPassword, String newPassword, String newPasswordConfirm) {
        MemberVO member = findById(memberId);
        if (member == null)
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        if (member.getPasswordHash() == null || member.getPasswordHash().isBlank()) {
            throw new IllegalStateException("SNS 전용 계정은 변경할 일반 로그인 비밀번호가 없습니다.");
        }
        if (currentPassword == null || currentPassword.isBlank()
                || !passwordEncoder.matches(currentPassword, member.getPasswordHash())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }
        validateNewPassword(newPassword, newPasswordConfirm);
        if (passwordEncoder.matches(newPassword, member.getPasswordHash())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 다르게 입력해주세요.");
        }
        if (memberDAO.updatePassword(memberId, passwordEncoder.encode(newPassword)) != 1) {
            throw new IllegalStateException("비밀번호를 변경하지 못했습니다.");
        }
    }

    @Override
    @Transactional
    public void withdraw(Long memberId, String currentPassword) {
        MemberVO member = findById(memberId);
        if (member == null)
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        if (!"ACTIVE".equalsIgnoreCase(member.getStatus())) {
            throw new IllegalStateException("이미 탈퇴했거나 사용할 수 없는 계정입니다.");
        }
        if (member.getPasswordHash() != null && !member.getPasswordHash().isBlank()) {
            if (currentPassword == null || currentPassword.isBlank()
                    || !passwordEncoder.matches(currentPassword, member.getPasswordHash())) {
                throw new IllegalArgumentException("회원 탈퇴를 위해 현재 비밀번호를 확인해주세요.");
            }
        }
        if (memberDAO.updateStatusWithdrawn(memberId) != 1) {
            throw new IllegalStateException("회원 탈퇴 처리에 실패했습니다.");
        }
    }

    private void validateNewPassword(String password, String confirm) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("새 비밀번호를 입력해주세요.");
        if (!password.equals(confirm))
            throw new IllegalArgumentException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=])[A-Za-z\\d!@#$%^&*()_+\\-=]{8,20}$")) {
            throw new IllegalArgumentException("비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자로 입력해주세요.");
        }
    }

    private String normalizePhone(String phone) {
        return phone == null ? null : phone.replaceAll("[^0-9]", "");
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private boolean isValidEmail(String email) {
        if (email == null)
            return false;
        String value = email.trim();
        return value.length() <= 150 && value.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private String createTemporaryPassword() {
        java.security.SecureRandom random = new java.security.SecureRandom();
        String upper = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lower = "abcdefghijkmnopqrstuvwxyz";
        String digit = "23456789";
        String special = "!@#$%";
        String all = upper + lower + digit + special;
        java.util.List<Character> chars = new java.util.ArrayList<>();
        chars.add(upper.charAt(random.nextInt(upper.length())));
        chars.add(lower.charAt(random.nextInt(lower.length())));
        chars.add(digit.charAt(random.nextInt(digit.length())));
        chars.add(special.charAt(random.nextInt(special.length())));
        while (chars.size() < 12)
            chars.add(all.charAt(random.nextInt(all.length())));
        java.util.Collections.shuffle(chars, random);
        StringBuilder password = new StringBuilder();
        chars.forEach(password::append);
        return password.toString();
    }
}