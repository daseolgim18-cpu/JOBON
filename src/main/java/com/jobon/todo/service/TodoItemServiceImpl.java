package com.jobon.todo.service;

/** [추가] 할 일 CRUD + 기업/공고/마감일 연계 Controller */
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.todo.dao.TodoItemDAO;
import com.jobon.todo.vo.TodoItemVO;

@Service
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemDAO dao;
    // [추가] 실제 활동 내역을 저장합니다.
    private final ActivityLogService activityLogService;

    public TodoItemServiceImpl(TodoItemDAO dao, ActivityLogService activityLogService) {
        this.dao = dao;
        this.activityLogService = activityLogService;
    }

    public List<TodoItemVO> list(Long memberId, String status) {
        return dao.selectList(memberId, status);
    }

    public TodoItemVO get(Long memberId, Long todoId) {
        TodoItemVO v = dao.selectOne(memberId, todoId);
        if (v == null)
            throw new IllegalArgumentException("데이터를 찾을 수 없습니다.");
        return v;
    }

    @Transactional
    public void create(TodoItemVO vo) {
        validate(vo);
        if (dao.insert(vo) != 1)
            throw new IllegalStateException("등록에 실패했습니다.");
        // [추가] 할일 등록 성공 후 활동 내역 저장
        activityLogService.record(vo.getMemberId(), "TODO", "CREATE", vo.getTodoId(),
                vo.getTitle() + " TODO 등록");
    }

    @Transactional
    public void update(TodoItemVO vo) {
        validate(vo);
        if (dao.update(vo) != 1)
            throw new IllegalStateException("수정에 실패했습니다.");
        // [추가] 완료 토글을 포함한 모든 할 일 수정 내역을 기록합니다.
        String suffix = "DONE".equals(vo.getStatus()) ? " TODO 완료" : " TODO 수정";
        activityLogService.record(vo.getMemberId(), "TODO", "UPDATE", vo.getTodoId(), vo.getTitle() + suffix);
    }

    @Transactional
    public void delete(Long memberId, Long todoId) {
        // [추가] 삭제 전에 활동 제목으로 사용할 할 일 제목을 조회합니다.
        TodoItemVO existing = get(memberId, todoId);
        if (dao.delete(memberId, todoId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
        activityLogService.record(memberId, "TODO", "DELETE", todoId, existing.getTitle() + " TODO 삭제");
    }

    private void validate(TodoItemVO vo) {
        if (vo == null || vo.getMemberId() == null)
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getTitle() == null || vo.getTitle().isBlank())
            throw new IllegalArgumentException("할 일을 입력해주세요.");
        if (vo.getPriority() == null || vo.getPriority().isBlank())
            vo.setPriority("MEDIUM");
        if (vo.getStatus() == null || vo.getStatus().isBlank())
            vo.setStatus("TODO");
    }
}
