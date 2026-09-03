package com.jobon.todo.service;

/** [추가] TodoItem 서비스 인터페이스 */
import java.util.List;
import java.time.LocalDate;
import com.jobon.todo.vo.TodoItemVO;

public interface TodoItemService {
    List<TodoItemVO> list(Long memberId, String status);

    TodoItemVO get(Long memberId, Long todoId);

    void create(TodoItemVO vo);

    void update(TodoItemVO vo);

    // [추가] 대시보드에서 TODO/진행중 항목을 완료 상태로만 변경합니다.
    void complete(Long memberId, Long todoId);

    void delete(Long memberId, Long todoId);

    void syncJobDeadline(Long memberId, Long jobId, LocalDate deadline);

    // [추가] 지원 현황의 다음 일정과 자동 TODO를 동기화합니다.
    void syncApplicationSchedule(com.jobon.apply.vo.ApplicationVO application);
}
