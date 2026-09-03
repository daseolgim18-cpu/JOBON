package com.jobon.job.service;

/** [추가] JobPosting 서비스 인터페이스 */
import java.util.List;
import com.jobon.job.vo.JobPostingVO;

public interface JobPostingService {
    List<JobPostingVO> list(Long memberId, String keyword, String jobRole, String sort);

    // [추가] 기업 상세 화면에서 COMPANY_ID 기준 연결 채용공고를 조회합니다.
    List<JobPostingVO> listByCompanyId(Long memberId, Long companyId);

    JobPostingVO get(Long memberId, Long jobId);

    void create(JobPostingVO vo);

    void update(JobPostingVO vo);

    void delete(Long memberId, Long jobId);
}
