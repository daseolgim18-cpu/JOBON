package com.jobon.mypage.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 마이페이지의 프로필 조회/수정 Controller입니다.
 * 로그인 세션의 MEMBER_ID를 기준으로 JOBON_MEMBER를 다시 조회하여
 * 일반 회원과 SNS 회원 모두 같은 프로필 화면을 사용하도록 처리합니다.
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jobon.member.service.MemberService;
import com.jobon.member.vo.MemberVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

    private static final long MAX_PROFILE_IMAGE_SIZE = 5L * 1024L * 1024L;

    private final MemberService memberService;

    /**
     * [수정] 프로필 이미지는 프로젝트 외부의 쓰기 가능한 폴더에 저장합니다.
     * application.properties에 jobon.upload.profile-dir 값을 따로 지정하지 않아도
     * 기본값 uploads/profiles 를 사용합니다.
     */
    @Value("${jobon.upload.profile-dir:uploads/profiles}")
    private String profileUploadDir;

    public MypageController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * [수정] 기존 CommonViewController의 단순 화면 반환 대신,
     * 로그인 회원의 MEMBER_ID로 DB를 조회하여 JSP에 member를 전달합니다.
     */
    @GetMapping({ "/mypage", "/mypage/profile" })
    public String profile(HttpSession session, Model model) {
        MemberVO member = getLoginMember(session);
        model.addAttribute("member", member);
        return "mypage/profile";
    }

    /**
     * [수정] 프로필 수정 화면에도 현재 DB 값을 전달하여 기존 값이 폼에 표시되도록 합니다.
     */
    @GetMapping("/mypage/profile/edit")
    public String profileEdit(HttpSession session, Model model) {
        MemberVO member = getLoginMember(session);
        model.addAttribute("member", member);
        return "mypage/profile-edit";
    }

    /** [추가] 비밀번호 변경 화면 */
    @GetMapping("/mypage/password")
    public String password(HttpSession session, Model model) {
        MemberVO member = getLoginMember(session);
        model.addAttribute("hasLocalPassword", memberService.hasLocalPassword(member.getMemberId()));
        return "mypage/password";
    }

    /** [추가] 현재 비밀번호를 검증한 뒤 BCrypt로 새 비밀번호를 저장합니다. */
    @PostMapping("/mypage/password")
    public String changePassword(@RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String newPasswordConfirm,
            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            memberService.changePassword(getLoginMember(session).getMemberId(), currentPassword, newPassword, newPasswordConfirm);
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 변경되었습니다.");
            return "redirect:/mypage/password";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mypage/password";
        }
    }

    /** [추가] 회원 탈퇴 확인 화면 */
    @GetMapping("/mypage/withdraw")
    public String withdraw(HttpSession session, Model model) {
        MemberVO member = getLoginMember(session);
        model.addAttribute("hasLocalPassword", memberService.hasLocalPassword(member.getMemberId()));
        return "mypage/withdraw";
    }

    /** [추가] 회원을 물리 삭제하지 않고 WITHDRAWN 상태로 전환한 뒤 세션을 종료합니다. */
    @PostMapping("/mypage/withdraw")
    public String withdrawProcess(@RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String confirmText,
            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (!"탈퇴합니다".equals(confirmText == null ? "" : confirmText.trim())) {
                throw new IllegalArgumentException("확인 문구에 '탈퇴합니다'를 입력해주세요.");
            }
            Long memberId = getLoginMember(session).getMemberId();
            memberService.withdraw(memberId, currentPassword);
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage", "회원 탈퇴가 완료되었습니다.");
            return "redirect:/main";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mypage/withdraw";
        }
    }

    /**
     * [수정] 프로필 수정 저장 처리입니다.
     * 닉네임/소개/관심직무/희망근무지와 선택한 프로필 이미지를 저장하고,
     * 갱신된 회원 정보를 세션에도 다시 넣어 화면 전체에서 최신 값을 사용하게 합니다.
     */
    @PostMapping("/mypage/profile/edit")
    public String updateProfile(@RequestParam String email,
            @RequestParam String nickname,
            @RequestParam(required = false) String introduction,
            @RequestParam(required = false) String interestJob,
            @RequestParam(required = false) String preferredLocation,
            @RequestParam(required = false) MultipartFile profileImage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        MemberVO loginMember = getLoginMember(session);

        if (nickname == null || nickname.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "닉네임을 입력해주세요.");
            return "redirect:/mypage/profile/edit";
        }

        MemberVO profile = new MemberVO();
        profile.setMemberId(loginMember.getMemberId());
        profile.setEmail(email);
        profile.setNickname(nickname);
        profile.setIntroduction(introduction);
        profile.setInterestJob(interestJob);
        profile.setPreferredLocation(preferredLocation);

        try {
            // [수정] 새 이미지를 선택한 경우에만 PROFILE_IMAGE_URL을 변경합니다.
            if (profileImage != null && !profileImage.isEmpty()) {
                profile.setProfileImageUrl(saveProfileImage(profileImage));
            }

            MemberVO updatedMember = memberService.updateProfile(profile);

            // [수정] 헤더/다른 화면에서도 최신 회원정보를 참조하도록 세션 갱신
            session.setAttribute("loginMember", updatedMember);
            session.setAttribute("loginMemberId", updatedMember.getMemberId());

            redirectAttributes.addFlashAttribute("successMessage", "프로필이 수정되었습니다.");
            return "redirect:/mypage";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mypage/profile/edit";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "프로필 이미지 저장 중 오류가 발생했습니다.");
            return "redirect:/mypage/profile/edit";
        }
    }

    /**
     * [수정] 세션 객체 자체를 화면에 그대로 사용하는 대신 MEMBER_ID로 최신 DB 정보를 조회합니다.
     * 일반 로그인과 SNS 로그인 모두 loginMemberId를 저장하고 있으므로 동일하게 동작합니다.
     */
    private MemberVO getLoginMember(HttpSession session) {
        Object memberIdValue = session.getAttribute("loginMemberId");
        if (memberIdValue == null && session.getAttribute("loginMember") instanceof MemberVO sessionMember) {
            memberIdValue = sessionMember.getMemberId();
        }

        if (!(memberIdValue instanceof Number number)) {
            throw new IllegalStateException("로그인 회원 정보를 확인할 수 없습니다.");
        }

        MemberVO member = memberService.findById(number.longValue());
        if (member == null) {
            throw new IllegalStateException("회원 정보를 찾을 수 없습니다.");
        }
        return member;
    }

    /**
     * [수정] JPG/PNG 파일만 허용하고 5MB 이하인지 검사한 뒤 고유 파일명으로 저장합니다.
     */
    private String saveProfileImage(MultipartFile file) throws IOException {
        if (file.getSize() > MAX_PROFILE_IMAGE_SIZE) {
            throw new IllegalArgumentException("프로필 이미지는 5MB 이하만 업로드할 수 있습니다.");
        }

        String contentType = file.getContentType();
        String extension;
        if ("image/jpeg".equalsIgnoreCase(contentType)) {
            extension = ".jpg";
        } else if ("image/png".equalsIgnoreCase(contentType)) {
            extension = ".png";
        } else {
            throw new IllegalArgumentException("프로필 이미지는 JPG 또는 PNG 파일만 사용할 수 있습니다.");
        }

        Path uploadPath = Paths.get(profileUploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT) + extension;
        Path target = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // WebConfig의 /uploads/profiles/** ResourceHandler와 연결되는 URL을 DB에 저장합니다.
        return "/uploads/profiles/" + fileName;
    }
}