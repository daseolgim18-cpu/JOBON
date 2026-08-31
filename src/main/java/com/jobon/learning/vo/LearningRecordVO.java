package com.jobon.learning.vo;

/** [추가] LearningRecordVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LearningRecordVO {

private Long learningId;
private Long memberId;
private String recordType;
private String subject;
private LocalDate learningDate;
private String content;
private String difficulty;
private String reflection;
private String techNames;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

}
