package com.jobon.project.vo;

/** [추가] PROJECT_FEATURE VO */
import lombok.Data;

@Data
public class ProjectFeatureVO {
    private Long featureId;
    private Long projectId;
    private String featureName;
    private String detail;
    private Integer sortOrder;
}
