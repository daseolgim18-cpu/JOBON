package com.jobon.savedsearch.vo;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * SAVED_SEARCH 테이블의 저장 검색 조건을 전달하는 VO입니다.
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SavedSearchVO {
    private Long searchId;
    private Long memberId;
    private String searchName;
    private String targetType;
    private String keyword;
    private String jobRole;
    private String careerType;
    private String region;
    private LocalDate postedFrom;
    private LocalDate postedTo;
    private LocalDate deadlineFrom;
    private LocalDate deadlineTo;
    private String extraConditions;
    private LocalDateTime createdAt;

    public String getTargetTypeLabel() {
        if (targetType == null) return "";
        return switch (targetType) {
            case "COMPANY" -> "기업";
            case "JOB" -> "채용공고";
            default -> "전체";
        };
    }

    public String getCareerTypeLabel() {
        if (careerType == null || careerType.isBlank()) return "";
        return switch (careerType) {
            case "NEW" -> "신입";
            case "CAREER" -> "경력";
            case "INTERN" -> "인턴";
            default -> careerType;
        };
    }

    public String getCreatedAtLabel() {
        return createdAt == null ? "" : createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public String getConditionSummary() {
        List<String> parts = new ArrayList<>();
        add(parts, keyword);
        add(parts, jobRole);
        add(parts, getCareerTypeLabel());
        add(parts, region);
        add(parts, extraConditions);
        if (postedFrom != null || postedTo != null) {
            parts.add("등록일 " + dateRange(postedFrom, postedTo));
        }
        if (deadlineFrom != null || deadlineTo != null) {
            parts.add("마감일 " + dateRange(deadlineFrom, deadlineTo));
        }
        return parts.isEmpty() ? "조건 없음" : String.join(" · ", parts);
    }

    private void add(List<String> parts, String value) {
        if (value != null && !value.isBlank()) parts.add(value.trim());
    }

    private String dateRange(LocalDate from, LocalDate to) {
        return (from == null ? "-" : from.toString()) + " ~ " + (to == null ? "-" : to.toString());
    }

    public Long getSearchId() { return searchId; }
    public void setSearchId(Long searchId) { this.searchId = searchId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getSearchName() { return searchName; }
    public void setSearchName(String searchName) { this.searchName = searchName; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
    public String getCareerType() { return careerType; }
    public void setCareerType(String careerType) { this.careerType = careerType; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public LocalDate getPostedFrom() { return postedFrom; }
    public void setPostedFrom(LocalDate postedFrom) { this.postedFrom = postedFrom; }
    public LocalDate getPostedTo() { return postedTo; }
    public void setPostedTo(LocalDate postedTo) { this.postedTo = postedTo; }
    public LocalDate getDeadlineFrom() { return deadlineFrom; }
    public void setDeadlineFrom(LocalDate deadlineFrom) { this.deadlineFrom = deadlineFrom; }
    public LocalDate getDeadlineTo() { return deadlineTo; }
    public void setDeadlineTo(LocalDate deadlineTo) { this.deadlineTo = deadlineTo; }
    public String getExtraConditions() { return extraConditions; }
    public void setExtraConditions(String extraConditions) { this.extraConditions = extraConditions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
