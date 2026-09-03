package com.jobon.savedsearch.dao;

/** SAVED_SEARCH 테이블 접근 DAO */
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jobon.savedsearch.vo.SavedSearchVO;

@Mapper
public interface SavedSearchDAO {
    List<SavedSearchVO> selectList(@Param("memberId") Long memberId);

    SavedSearchVO selectOne(@Param("memberId") Long memberId, @Param("searchId") Long searchId);

    int insert(SavedSearchVO vo);

    int delete(@Param("memberId") Long memberId, @Param("searchId") Long searchId);
}
