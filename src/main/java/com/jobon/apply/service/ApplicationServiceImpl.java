package com.jobon.apply.service;

/** [추가] Application CRUD 비즈니스 로직 */
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.apply.dao.ApplicationDAO;
import com.jobon.apply.vo.ApplicationVO;
import com.jobon.todo.service.TodoItemService;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private static final Set<String> ALLOWED_STATUSES = Set.of("INTEREST", "APPLIED", "DOCUMENT", "CODING_TEST", "INTERVIEW", "OFFER", "REJECTED");
    private final ApplicationDAO dao;
    // [추가] 지원 현황 CRUD 성공 시 실제 활동 내역을 저장합니다.
    private final ActivityLogService activityLogService;
    private final TodoItemService todoItemService;

    public ApplicationServiceImpl(ApplicationDAO dao, ActivityLogService activityLogService, TodoItemService todoItemService) {
        this.dao = dao;
        this.activityLogService = activityLogService;
        this.todoItemService = todoItemService;
    }

    public List<ApplicationVO> list(Long memberId, String keyword, String status, String sort) {
        return dao.selectList(memberId, keyword, status, sort);
    }

    public ApplicationVO get(Long memberId, Long applicationId) {
        ApplicationVO v = dao.selectOne(memberId, applicationId);
        if (v == null)
            throw new IllegalArgumentException("데이터를 찾을 수 없습니다.");
        return v;
    }

    @Override
    public ApplicationVO getByJob(Long memberId, Long jobId) {
        if (memberId == null || jobId == null) return null;
        return dao.selectByJob(memberId, jobId);
    }

    @Transactional
    public void create(ApplicationVO vo) {
        validate(vo);
        if (dao.insert(vo) != 1)
            throw new IllegalStateException("등록에 실패했습니다.");
        // [추가] JOIN 조회를 다시 수행해 기업명/공고명을 포함한 활동 제목을 만듭니다.
        ApplicationVO saved = dao.selectOne(vo.getMemberId(), vo.getApplicationId());
        activityLogService.record(vo.getMemberId(), "APPLICATION", "CREATE", vo.getApplicationId(),
                applicationTitle(saved, "지원 현황 등록 · " + saved.getStatusLabel()));
        todoItemService.syncApplicationSchedule(saved);
    }

    @Transactional
    public void update(ApplicationVO vo) {
        validate(vo);
        ApplicationVO before = get(vo.getMemberId(), vo.getApplicationId());
        if (dao.update(vo) != 1)
            throw new IllegalStateException("수정에 실패했습니다.");
        // [추가] 수정 후 최신 기업명/공고명 기준으로 활동 내역 저장
        ApplicationVO saved = dao.selectOne(vo.getMemberId(), vo.getApplicationId());
        if (!java.util.Objects.equals(before.getStatus(), saved.getStatus())) {
            activityLogService.record(vo.getMemberId(), "APPLICATION", "UPDATE", vo.getApplicationId(),
                    applicationTitle(saved, "상태 변경: " + before.getStatusLabel() + " → " + saved.getStatusLabel()));
        } else {
            activityLogService.record(vo.getMemberId(), "APPLICATION", "UPDATE", vo.getApplicationId(),
                    applicationTitle(saved, "지원 현황 수정"));
        }
        todoItemService.syncApplicationSchedule(saved);
    }

    @Transactional
    public void delete(Long memberId, Long applicationId) {
        // [추가] 삭제 전 기업명/공고명을 보관하여 삭제 활동 제목에 사용합니다.
        ApplicationVO existing = get(memberId, applicationId);
        existing.setNextScheduleAt(null);
        todoItemService.syncApplicationSchedule(existing);
        if (dao.delete(memberId, applicationId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
        activityLogService.record(memberId, "APPLICATION", "DELETE", applicationId,
                applicationTitle(existing, "지원 현황 삭제"));
    }

    // [추가] 지원 활동 제목을 한 형식으로 통일합니다.
    private String applicationTitle(ApplicationVO vo, String actionText) {
        if (vo == null) return actionText;
        String company = vo.getCompanyName() == null || vo.getCompanyName().isBlank() ? "기업 미지정" : vo.getCompanyName();
        String job = vo.getJobTitle() == null || vo.getJobTitle().isBlank() ? "채용공고" : vo.getJobTitle();
        return company + " · " + job + " " + actionText;
    }

    private void validate(ApplicationVO vo) {
        if (vo == null || vo.getMemberId() == null)
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getJobId() == null)
            throw new IllegalArgumentException("채용공고를 선택해주세요.");
        if (dao.countOwnedJob(vo.getMemberId(), vo.getJobId()) != 1)
            throw new IllegalArgumentException("현재 회원이 등록한 채용공고만 지원 현황에 연결할 수 있습니다.");
        if (vo.getStatus() == null || vo.getStatus().isBlank()) {
            vo.setStatus("INTEREST");
        } else {
            vo.setStatus(vo.getStatus().trim().toUpperCase());
            if (!ALLOWED_STATUSES.contains(vo.getStatus())) {
                throw new IllegalArgumentException("지원 상태 값이 올바르지 않습니다.");
            }
        }
    }
}
