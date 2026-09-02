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

    int insertRecommendation(AiExperienceRecommendVO v);

    List<AiExperienceRecommendVO> selectRecommendations(@Param("analysisId") Long id);

    int updateRecommendationSaved(@Param("memberId") Long memberId,
            @Param("recommendId") Long id, @Param("savedYn") String yn);

    List<String> selectMemberTechs(@Param("memberId") Long memberId);

    List<String> selectProjectTechs(@Param("projectId") Long projectId);
}
