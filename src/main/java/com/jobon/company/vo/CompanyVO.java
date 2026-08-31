package com.jobon.company.vo;

/** [추가] CompanyVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CompanyVO {

private Long companyId;
private Long memberId;
private String companyName;
private String companyType;
private String industry;
private String jobField;
private String careerUrl;
private String logoUrl;
private String memo;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

}
