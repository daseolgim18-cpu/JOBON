package com.jobon.mypage.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 마이페이지의 SNS 연동 계정 관리 화면 Controller입니다.
 * 현재 로그인한 MEMBER_ID를 기준으로 SOCIAL_ACCOUNT를 조회하여
 * Google/Naver/Kakao의 실제 연동 상태와 이메일을 화면에 전달합니다.
 */
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jobon.member.service.MemberService;
import com.jobon.member.vo.MemberVO;
import com.jobon.social.service.SocialLoginService;
import com.jobon.social.vo.SocialAccountVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageAccountsController {

    private final MemberService memberService;
    private final SocialLoginService socialLoginService;

    public MypageAccountsController(MemberService memberService, SocialLoginService socialLoginService) {
        this.memberService = memberService;
        this.socialLoginService = socialLoginService;
    }

    /**
     * [추가] 하드코딩된 샘플 계정 대신 SOCIAL_ACCOUNT의 실제 연동 정보를 조회합니다.
     * JSP에서는 provider별 객체 존재 여부로 연동/미연동 상태를 표시합니다.
     */
    @GetMapping("/mypage/accounts")
    public String accounts(HttpSession session, Model model) {
        MemberVO member = getLoginMember(session);
        List<SocialAccountVO> accounts = socialLoginService.findLinkedAccounts(member.getMemberId());

        model.addAttribute("member", member);
        model.addAttribute("googleAccount", findByProvider(accounts, "GOOGLE"));
        model.addAttribute("naverAccount", findByProvider(accounts, "NAVER"));
        model.addAttribute("kakaoAccount", findByProvider(accounts, "KAKAO"));
        model.addAttribute("linkedAccountCount", accounts.size());
        return "mypage/accounts";
    }

    // [추가] 세션의 MEMBER_ID로 최신 JOBON_MEMBER 정보를 조회합니다.
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

    // [추가] 조회된 연동 목록에서 화면에 표시할 SNS 제공자 계정을 찾습니다.
    private SocialAccountVO findByProvider(List<SocialAccountVO> accounts, String provider) {
        if (accounts == null) {
            return null;
        }
        return accounts.stream()
                .filter(account -> provider.equalsIgnoreCase(account.getProvider()))
                .findFirst()
                .orElse(null);
    }
}