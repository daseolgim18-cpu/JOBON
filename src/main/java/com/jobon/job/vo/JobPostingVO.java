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
private String employmentType;
private String region;
private String originalText;
private String memo;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

}
