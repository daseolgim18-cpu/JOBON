package com.jobon.learning.dao;

/** [추가] 성장 기록 CRUD 및 기술 키워드 DAO */
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.jobon.learning.vo.LearningRecordVO;

@Mapper
public interface LearningRecordDAO {
    List<LearningRecordVO> selectList(@Param("memberId") Long memberId);

    LearningRecordVO selectOne(@Param("memberId") Long memberId, @Param("learningId") Long learningId);

    int insert(LearningRecordVO vo);

    int update(LearningRecordVO vo);

    int delete(@Param("memberId") Long memberId, @Param("learningId") Long learningId);

    void mergeTech(@Param("techName") String techName);

    Long selectTechId(@Param("techName") String techName);

    int insertLearningTech(@Param("learningId") Long learningId, @Param("techId") Long techId);

    int deleteLearningTech(@Param("learningId") Long learningId);

    List<String> selectTechNames(@Param("learningId") Long learningId);
}
