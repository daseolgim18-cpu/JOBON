package com.jobon.apply.vo;

/** [추가] ApplicationVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApplicationVO {

private Long applicationId;
private Long memberId;
private Long jobId;
private String companyName;
private String jobTitle;
private String status;
private LocalDate appliedDate;
private LocalDateTime nextScheduleAt;
private String memo;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

/** [추가] 화면에서 지원 상태 코드를 한글로 표시합니다. */
public String getStatusLabel() {
    if (status == null) return "";
    return switch (status) {
        case "INTEREST" -> "관심";
        case "APPLIED" -> "지원완료";
        case "DOCUMENT" -> "서류";
        case "INTERVIEW" -> "면접";
        case "OFFER" -> "합격";
        case "REJECTED" -> "불합격";
        default -> status;
    };
}

}
