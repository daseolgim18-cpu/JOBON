package com.jobon.job.dao;

/** [추가] 채용공고 CRUD DAO */
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.jobon.job.vo.JobPostingVO;

@Mapper
public interface JobPostingDAO {
    List<JobPostingVO> selectList(@Param("memberId") Long memberId, @Param("keyword") String keyword,
            @Param("jobRole") String jobRole, @Param("sort") String sort);

    // [추가] 기업 상세 화면에서 해당 기업과 FK로 연결된 실제 채용공고만 조회합니다.
    List<JobPostingVO> selectByCompanyId(@Param("memberId") Long memberId, @Param("companyId") Long companyId);

    JobPostingVO selectOne(@Param("memberId") Long memberId, @Param("jobId") Long jobId);

    int insert(JobPostingVO vo);

    int countOwnedCompany(@Param("memberId") Long memberId, @Param("companyId") Long companyId);

    int update(JobPostingVO vo);

    int delete(@Param("memberId") Long memberId, @Param("jobId") Long jobId);
}
