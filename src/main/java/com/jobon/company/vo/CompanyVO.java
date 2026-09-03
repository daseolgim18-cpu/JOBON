package com.jobon.company.vo;

/** [추가] CompanyVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class CompanyVO {

private Long companyId;
private Long memberId;
private String companyName;
private String companyType;
private String industry;
private String jobField;
// [추가] 메모에 섞여 있던 기업 업종/홈페이지/사업내용/주소를 COMPANY 컬럼과 1:1로 관리합니다.
private String businessType;
private String homepageUrl;
private String businessDescription;
private String address;
private String careerUrl;
private String logoUrl;
private String memo;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

/** [추가] 목록 화면에서 TIMESTAMP의 초/밀리초를 제거한 등록일을 표시합니다. */
public String getCreatedAtLabel() {
    if (createdAt == null) return "";
    return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
}

/** [추가] 기업 상세 화면에서 DB UPDATED_AT을 동일한 형식으로 표시합니다. */
public String getUpdatedAtLabel() {
    if (updatedAt == null) return "";
    return updatedAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
}

}

