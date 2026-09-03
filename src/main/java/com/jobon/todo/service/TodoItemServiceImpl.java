package com.jobon.todo.service;

/** [추가] 할 일 CRUD + 기업/공고/마감일 연계 Controller */
import java.util.List;
import java.util.Set;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.todo.dao.TodoItemDAO;
import com.jobon.todo.vo.TodoItemVO;

@Service
public class TodoItemServiceImpl implements TodoItemService {
    private static final Set<String> ALLOWED_PRIORITIES = Set.of("HIGH", "MEDIUM", "LOW");
    private static final Set<String> ALLOWED_STATUSES = Set.of("TODO", "DOING", "DONE");
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

    // [추가] 목록에서는 제목/마감일/연결 정보 등을 덮어쓰지 않고 상태 관련 컬럼만 변경합니다.
    @Override
    @Transactional
    public void changeStatus(Long memberId, Long todoId, String status) {
        if (memberId == null || todoId == null)
            throw new IllegalArgumentException("TODO 상태 변경 정보가 없습니다.");
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("변경할 TODO 상태를 선택해주세요.");

        String normalizedStatus = status.trim().toUpperCase();
        if (!ALLOWED_STATUSES.contains(normalizedStatus))
            throw new IllegalArgumentException("TODO 상태 값이 올바르지 않습니다.");

        TodoItemVO existing = get(memberId, todoId);
        if (normalizedStatus.equals(existing.getStatus())) return;

        if (dao.updateStatus(memberId, todoId, normalizedStatus) != 1)
            throw new IllegalStateException("TODO 상태 변경에 실패했습니다.");

        activityLogService.record(memberId, "TODO", "UPDATE", todoId,
                existing.getTitle() + " TODO 상태 변경 ("
                        + statusLabel(existing.getStatus()) + " → " + statusLabel(normalizedStatus) + ")");
    }

    // [추가] 대시보드 완료 처리는 toggle이 아니라 TODO/DOING -> DONE 단방향으로 처리합니다.
    // 이미 완료된 항목에 요청이 다시 들어와도 중복 활동 로그를 남기지 않습니다.
    @Override
    @Transactional
    public void complete(Long memberId, Long todoId) {
        TodoItemVO existing = get(memberId, todoId);
        if ("DONE".equals(existing.getStatus())) return;

        if (dao.complete(memberId, todoId) != 1)
            throw new IllegalStateException("완료 처리에 실패했습니다.");

        activityLogService.record(memberId, "TODO", "UPDATE", todoId,
                existing.getTitle() + " TODO 완료");
    }

    @Transactional
    public void delete(Long memberId, Long todoId) {
        // [추가] 삭제 전에 활동 제목으로 사용할 할 일 제목을 조회합니다.
        TodoItemVO existing = get(memberId, todoId);
        if (dao.delete(memberId, todoId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
        activityLogService.record(memberId, "TODO", "DELETE", todoId, existing.getTitle() + " TODO 삭제");
    }

    @Override
    @Transactional
    public void syncJobDeadline(Long memberId, Long jobId, LocalDate deadline) {
        if (memberId == null || jobId == null) return;
        dao.updateDueDateByJobId(memberId, jobId, deadline);
    }

    @Override
    @Transactional
    public void syncApplicationSchedule(com.jobon.apply.vo.ApplicationVO application) {
        if (application == null || application.getMemberId() == null || application.getApplicationId() == null) return;
        String marker = "[AUTO_APPLICATION:" + application.getApplicationId() + "]";
        if (application.getNextScheduleAt() == null) {
            dao.deleteAutoApplicationTodo(application.getMemberId(), marker);
            return;
        }

        TodoItemVO todo = dao.selectAutoApplicationTodo(application.getMemberId(), marker);
        boolean isNew = todo == null;
        if (isNew) {
            todo = new TodoItemVO();
            todo.setMemberId(application.getMemberId());
            todo.setStatus("TODO");
            todo.setPriority("HIGH");
        }
        todo.setCompanyId(application.getCompanyId());
        todo.setJobId(application.getJobId());
        todo.setDueDate(application.getNextScheduleAt().toLocalDate());
        String company = application.getCompanyName() == null || application.getCompanyName().isBlank() ? "지원" : application.getCompanyName();
        todo.setTitle(company + " " + application.getStatusLabel() + " 일정 준비");
        todo.setMemo(marker + "\n지원 현황의 다음 일정에서 자동 생성된 TODO입니다.");
        if (isNew) {
            create(todo);
        } else if (!"DONE".equals(todo.getStatus())) {
            update(todo);
        }
    }

    private void validate(TodoItemVO vo) {
        if (vo == null || vo.getMemberId() == null)
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getTitle() == null || vo.getTitle().isBlank())
            throw new IllegalArgumentException("할 일을 입력해주세요.");
        if (vo.getTitle().trim().length() > 200)
            throw new IllegalArgumentException("할 일은 200자 이하로 입력해주세요.");
        vo.setTitle(vo.getTitle().trim());

        if (vo.getPriority() == null || vo.getPriority().isBlank()) vo.setPriority("MEDIUM");
        vo.setPriority(vo.getPriority().trim().toUpperCase());
        if (!ALLOWED_PRIORITIES.contains(vo.getPriority()))
            throw new IllegalArgumentException("TODO 우선순위 값이 올바르지 않습니다.");

        if (vo.getStatus() == null || vo.getStatus().isBlank()) vo.setStatus("TODO");
        vo.setStatus(vo.getStatus().trim().toUpperCase());
        if (!ALLOWED_STATUSES.contains(vo.getStatus()))
            throw new IllegalArgumentException("TODO 상태 값이 올바르지 않습니다.");

        if (vo.getCompanyId() != null && dao.countOwnedCompany(vo.getMemberId(), vo.getCompanyId()) != 1)
            throw new IllegalArgumentException("현재 회원이 등록한 기업만 연결할 수 있습니다.");
        if (vo.getJobId() != null && dao.countOwnedJob(vo.getMemberId(), vo.getJobId()) != 1)
            throw new IllegalArgumentException("현재 회원이 등록한 채용공고만 연결할 수 있습니다.");
    }

    private String statusLabel(String status) {
        if (status == null) return "";
        return switch (status) {
            case "TODO" -> "할 일";
            case "DOING" -> "진행 중";
            case "DONE" -> "완료";
            default -> status;
        };
    }
}
