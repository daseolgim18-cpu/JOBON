package com.jobon.search.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 헤더 통합 검색 및 저장 검색어 실행 결과를 처리합니다.
 * 현재 ERD에 존재하는 기업/채용공고 컬럼 범위 안에서 검색 조건을 적용합니다.
 */
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobon.common.util.SessionMemberUtil;
import com.jobon.company.service.CompanyService;
import com.jobon.company.vo.CompanyVO;
import com.jobon.job.service.JobPostingService;
import com.jobon.job.vo.JobPostingVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class SearchController {
    private final CompanyService companies;
    private final JobPostingService jobs;

    public SearchController(CompanyService companies, JobPostingService jobs) {
        this.companies = companies;
        this.jobs = jobs;
    }

    @GetMapping("/search/result")
    public String result(@RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ALL") String targetType,
            @RequestParam(required = false) String jobRole,
            @RequestParam(required = false) String careerType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) LocalDate postedFrom,
            @RequestParam(required = false) LocalDate postedTo,
            @RequestParam(required = false) LocalDate deadlineFrom,
            @RequestParam(required = false) LocalDate deadlineTo,
            @RequestParam(required = false) String extraConditions,
            HttpSession session, Model model) {
        Long memberId = SessionMemberUtil.requireMemberId(session);
        String normalizedTarget = normalizeTarget(targetType);

        List<CompanyVO> companyResults = "JOB".equals(normalizedTarget)
                ? List.of()
                : companies.list(memberId, trimToNull(keyword), null);

        List<JobPostingVO> jobResults = "COMPANY".equals(normalizedTarget)
                ? List.of()
                : jobs.list(memberId, trimToNull(keyword), trimToNull(jobRole), "latest").stream()
                        .filter(j -> matchesRegion(j, region))
                        .filter(j -> matchesCareer(j, careerType))
                        .filter(j -> between(j.getPostedDate(), postedFrom, postedTo))
                        .filter(j -> between(j.getDeadline(), deadlineFrom, deadlineTo))
                        .filter(j -> matchesExtra(j, extraConditions))
                        .toList();

        model.addAttribute("keyword", trimToNull(keyword));
        model.addAttribute("targetType", normalizedTarget);
        model.addAttribute("jobRole", trimToNull(jobRole));
        model.addAttribute("careerType", trimToNull(careerType));
        model.addAttribute("region", trimToNull(region));
        model.addAttribute("postedFrom", postedFrom);
        model.addAttribute("postedTo", postedTo);
        model.addAttribute("deadlineFrom", deadlineFrom);
        model.addAttribute("deadlineTo", deadlineTo);
        model.addAttribute("extraConditions", trimToNull(extraConditions));
        model.addAttribute("companyResults", companyResults);
        model.addAttribute("jobResults", jobResults);
        model.addAttribute("totalCount", companyResults.size() + jobResults.size());
        return "search/result";
    }

    private boolean matchesRegion(JobPostingVO job, String region) {
        return blank(region) || contains(job.getRegion(), region);
    }

    private boolean matchesCareer(JobPostingVO job, String careerType) {
        if (blank(careerType)) return true;
        String word = switch (careerType.toUpperCase(Locale.ROOT)) {
            case "NEW" -> "신입";
            case "CAREER" -> "경력";
            case "INTERN" -> "인턴";
            default -> careerType;
        };
        return contains(job.getTitle(), word) || contains(job.getJobRole(), word)
                || contains(job.getEmploymentType(), word) || contains(job.getOriginalText(), word);
    }

    private boolean matchesExtra(JobPostingVO job, String extra) {
        if (blank(extra)) return true;
        String[] words = extra.split("[,/\\s]+");
        for (String word : words) {
            if (blank(word)) continue;
            if (contains(job.getEmploymentType(), word) || contains(job.getRegion(), word)
                    || contains(job.getTitle(), word) || contains(job.getOriginalText(), word)) return true;
        }
        return false;
    }

    private boolean between(LocalDate value, LocalDate from, LocalDate to) {
        if (from == null && to == null) return true;
        if (value == null) return false;
        return (from == null || !value.isBefore(from)) && (to == null || !value.isAfter(to));
    }

    private boolean contains(String source, String keyword) {
        return source != null && keyword != null
                && source.toLowerCase(Locale.ROOT).contains(keyword.trim().toLowerCase(Locale.ROOT));
    }

    private String normalizeTarget(String targetType) {
        if (targetType == null) return "ALL";
        String value = targetType.trim().toUpperCase(Locale.ROOT);
        return switch (value) {
            case "COMPANY", "JOB" -> value;
            default -> "ALL";
        };
    }

    private String trimToNull(String value) {
        return blank(value) ? null : value.trim();
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
