package com.jobon.company.dao;

/** [추가] 기업 CRUD DAO */
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.jobon.company.vo.CompanyVO;

@Mapper
public interface CompanyDAO {
    List<CompanyVO> selectList(@Param("memberId") Long memberId, @Param("keyword") String keyword,
            @Param("companyType") String companyType);

    CompanyVO selectOne(@Param("memberId") Long memberId, @Param("companyId") Long companyId);

    int insert(CompanyVO vo);

    int update(CompanyVO vo);

    int delete(@Param("memberId") Long memberId, @Param("companyId") Long companyId);
}
