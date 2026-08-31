package com.jobon.apply.dao;

/** [추가] 지원 현황 CRUD/필터 DAO */
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.jobon.apply.vo.ApplicationVO;

@Mapper
public interface ApplicationDAO {
    List<ApplicationVO> selectList(@Param("memberId") Long memberId, @Param("keyword") String keyword,
            @Param("status") String status, @Param("sort") String sort);

    ApplicationVO selectOne(@Param("memberId") Long memberId, @Param("applicationId") Long applicationId);

    int insert(ApplicationVO vo);

    int update(ApplicationVO vo);

    int delete(@Param("memberId") Long memberId, @Param("applicationId") Long applicationId);
}
