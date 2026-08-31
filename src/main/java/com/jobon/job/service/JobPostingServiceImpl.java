package com.jobon.job.service;

/** [추가] JobPosting CRUD 비즈니스 로직 */
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.job.dao.JobPostingDAO;
import com.jobon.job.vo.JobPostingVO;

@Service
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingDAO dao;

    public JobPostingServiceImpl(JobPostingDAO dao) {
        this.dao = dao;
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
    }

    @Transactional
    public void update(JobPostingVO vo) {
        validate(vo);
        if (dao.update(vo) != 1)
            throw new IllegalStateException("수정에 실패했습니다.");
    }

    @Transactional
    public void delete(Long memberId, Long jobId) {
        if (dao.delete(memberId, jobId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
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
