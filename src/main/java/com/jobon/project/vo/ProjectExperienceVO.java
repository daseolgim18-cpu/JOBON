package com.jobon.project.vo;

/** [추가] ProjectExperienceVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProjectExperienceVO {

private Long projectId;
private Long memberId;
private String projectName;
private String organization;
private String roleName;
private LocalDate startDate;
private LocalDate endDate;
private String description;
private String projectUrl;
private String techNames;
private java.util.List<ProjectFeatureVO> features = new java.util.ArrayList<>();
private java.util.List<ProjectTroubleVO> troubles = new java.util.ArrayList<>();
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

}
