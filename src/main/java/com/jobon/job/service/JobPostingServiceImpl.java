package com.jobon.job.service;

/** [추가] JobPosting CRUD 비즈니스 로직 */
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.job.dao.JobPostingDAO;
import com.jobon.job.vo.JobPostingVO;

@Service
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingDAO dao;
    // [추가] 채용공고 CRUD 성공 시 실제 활동 내역을 저장합니다.
    private final ActivityLogService activityLogService;

    public JobPostingServiceImpl(JobPostingDAO dao, ActivityLogService activityLogService) {
        this.dao = dao;
        this.activityLogService = activityLogService;
    }

    public List<JobPostingVO> list(Long memberId, String keyword, String jobRole, String sort) {
        return dao.selectList(memberId, keyword, jobRole, sort);
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
        if (vo.getJobRole() == null || vo.getJobRole().isBlank())
            throw new IllegalArgumentException("채용 직무를 입력해주세요.");
    }
}
