package com.jobon.job.dao;

/** [추가] 채용공고 CRUD DAO */
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.jobon.job.vo.JobPostingVO;

@Mapper
public interface JobPostingDAO {
    List<JobPostingVO> selectList(@Param("memberId") Long memberId, @Param("keyword") String keyword,
            @Param("jobRole") String jobRole, @Param("sort") String sort);

    JobPostingVO selectOne(@Param("memberId") Long memberId, @Param("jobId") Long jobId);

    int insert(JobPostingVO vo);

    int update(JobPostingVO vo);

    int delete(@Param("memberId") Long memberId, @Param("jobId") Long jobId);
}
