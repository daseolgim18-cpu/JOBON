package com.jobon.todo.vo;

/** [추가] TodoItemVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TodoItemVO {

private Long todoId;
private Long memberId;
private Long companyId;
private Long jobId;
private String companyName;
private String jobTitle;
private String title;
private String priority;
private LocalDate dueDate;
private String status;
private String memo;
private LocalDateTime completedAt;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

}
