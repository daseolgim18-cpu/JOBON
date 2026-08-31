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

}
