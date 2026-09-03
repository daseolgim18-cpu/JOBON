package com.jobon.ai.dao;

/** [추가] AI 분석/기술/추천 저장 DAO */
import java.util.*;
import org.apache.ibatis.annotations.*;
import com.jobon.ai.vo.*;

@Mapper
public interface AiAnalysisDAO {
    List<AiAnalysisVO> selectList(@Param("memberId") Long memberId);

    AiAnalysisVO selectOne(@Param("memberId") Long memberId, @Param("analysisId") Long analysisId);

    AiAnalysisVO selectLatestByJob(@Param("memberId") Long memberId, @Param("jobId") Long jobId);

    int insertAnalysis(AiAnalysisVO v);

    int completeAnalysis(AiAnalysisVO v);

    int failAnalysis(AiAnalysisVO v);

    int deleteTechs(@Param("analysisId") Long id);

    int insertTech(AiJobTechVO v);

    List<AiJobTechVO> selectTechs(@Param("analysisId") Long id);

    int deleteRecommendations(@Param("analysisId") Long id);

    int deleteAnalysis(@Param("memberId") Long memberId, @Param("analysisId") Long analysisId);

    int insertRecommendation(AiExperienceRecommendVO v);

    List<AiExperienceRecommendVO> selectRecommendations(@Param("analysisId") Long id);

    /** [추가] 로그인 회원이 저장한 자소서 경험 추천만 조회합니다. */
    List<AiExperienceRecommendVO> selectSavedRecommendations(@Param("memberId") Long memberId);

    int updateRecommendationSaved(@Param("memberId") Long memberId,
            @Param("recommendId") Long id, @Param("savedYn") String yn);

    List<String> selectMemberTechs(@Param("memberId") Long memberId);

    List<String> selectProjectTechs(@Param("projectId") Long projectId);

    // [추가] AI 분석 전 데이터 품질 안내용 건수
    int countMemberProjects(@Param("memberId") Long memberId);
    int countMemberLearningRecords(@Param("memberId") Long memberId);
}
