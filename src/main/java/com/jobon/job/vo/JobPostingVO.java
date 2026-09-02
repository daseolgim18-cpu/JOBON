package com.jobon.job.vo;

/** [추가] JobPostingVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class JobPostingVO {

private Long jobId;
private Long memberId;
private Long companyId;
private String companyName;
private String title;
private String jobRole;
private String sourceUrl;
private LocalDate postedDate;
private LocalDate deadline;
// [추가] 대시보드에서 마감일까지 남은 일수를 표시하기 위한 화면 전용 값입니다.
// DB 컬럼과 연결하지 않으며 DashboardController에서 계산합니다.
private Integer daysUntilDeadline;
private String employmentType;
private String region;
private String originalText;
private String memo;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

}
