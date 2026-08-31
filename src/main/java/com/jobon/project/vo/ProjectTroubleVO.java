package com.jobon.project.vo;

/** [추가] PROJECT_TROUBLE VO */
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProjectTroubleVO {
    private Long troubleId;
    private Long projectId;
    private String title;
    private String problem;
    private String cause;
    private String solution;
    private String result;
    private LocalDateTime createdAt;
}
