package com.jobon.job.service;

/** [추가] JobPosting CRUD 비즈니스 로직 */
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.job.dao.JobPostingDAO;
import com.jobon.job.vo.JobPostingVO;
import com.jobon.todo.service.TodoItemService;

@Service
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingDAO dao;
    // [추가] 채용공고 CRUD 성공 시 실제 활동 내역을 저장합니다.
    private final ActivityLogService activityLogService;
    // [추가] 채용공고 마감일과 미완료 TODO 마감일을 함께 관리합니다.
    private final TodoItemService todoItemService;

    public JobPostingServiceImpl(JobPostingDAO dao, ActivityLogService activityLogService,
            TodoItemService todoItemService) {
        this.dao = dao;
        this.activityLogService = activityLogService;
        this.todoItemService = todoItemService;
    }

    public List<JobPostingVO> list(Long memberId, String keyword, String jobRole, String sort) {
        return dao.selectList(memberId, keyword, jobRole, sort);
    }

    // [추가] 기업 상세 화면의 연결 채용공고는 기업명 검색이 아니라 COMPANY_ID FK로 정확히 조회합니다.
    public List<JobPostingVO> listByCompanyId(Long memberId, Long companyId) {
        if (memberId == null || companyId == null)
            throw new IllegalArgumentException("기업 조회 정보가 없습니다.");
        return dao.selectByCompanyId(memberId, companyId);
    }

    public JobPostingVO get(Long memberId, Long jobId) {
        JobPostingVO v = dao.selectOne(memberId, jobId);
        if (v == null)
            throw new IllegalArgumentException("데이터를 찾을 수 없습니다.");
        return v;
    }

    @Transactional
    public void create(JobPostingVO vo) {
        validate(vo);
        if (dao.insert(vo) != 1)
            throw new IllegalStateException("등록에 실패했습니다.");
        // [추가] 채용공고 등록 성공 후 활동 내역 저장
        activityLogService.record(vo.getMemberId(), "JOB", "CREATE", vo.getJobId(),
                vo.getTitle() + " 채용공고 등록");
    }

    @Transactional
    public void update(JobPostingVO vo) {
        validate(vo);
        if (dao.update(vo) != 1)
            throw new IllegalStateException("수정에 실패했습니다.");
        // [추가] 공고 수정 시 이미 만들어진 미완료 연계 TODO의 마감일도 갱신합니다.
        todoItemService.syncJobDeadline(vo.getMemberId(), vo.getJobId(), vo.getDeadline());
        // [추가] 채용공고 수정 성공 후 활동 내역 저장
        activityLogService.record(vo.getMemberId(), "JOB", "UPDATE", vo.getJobId(),
                vo.getTitle() + " 채용공고 수정");
    }

    @Transactional
    public void delete(Long memberId, Long jobId) {
        // [추가] 삭제 전에 활동 제목으로 사용할 채용공고명을 조회합니다.
        JobPostingVO existing = get(memberId, jobId);
        if (dao.delete(memberId, jobId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
        activityLogService.record(memberId, "JOB", "DELETE", jobId,
                existing.getTitle() + " 채용공고 삭제");
    }

    private void validate(JobPostingVO vo) {
        if (vo == null || vo.getMemberId() == null)
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getTitle() == null || vo.getTitle().isBlank())
            throw new IllegalArgumentException("공고명을 입력해주세요.");
        if (vo.getTitle().trim().length() > 200) throw new IllegalArgumentException("공고명은 200자 이하로 입력해주세요.");
        vo.setTitle(vo.getTitle().trim());
        if (vo.getJobRole() == null || vo.getJobRole().isBlank())
            throw new IllegalArgumentException("채용 직무를 입력해주세요.");
        if (vo.getJobRole().trim().length() > 120) throw new IllegalArgumentException("채용 직무는 120자 이하로 입력해주세요.");
        vo.setJobRole(vo.getJobRole().trim());
        if (vo.getCompanyId() != null && dao.countOwnedCompany(vo.getMemberId(), vo.getCompanyId()) != 1)
            throw new IllegalArgumentException("현재 회원이 등록한 기업만 연결할 수 있습니다.");
        if (vo.getPostedDate() != null && vo.getDeadline() != null && vo.getPostedDate().isAfter(vo.getDeadline()))
            throw new IllegalArgumentException("채용공고 등록일은 마감일보다 늦을 수 없습니다.");
        if (vo.getSourceUrl() != null && !vo.getSourceUrl().isBlank()
                && !(vo.getSourceUrl().startsWith("http://") || vo.getSourceUrl().startsWith("https://")))
            throw new IllegalArgumentException("채용공고 출처 URL은 http:// 또는 https://로 시작해야 합니다.");
    }
}
