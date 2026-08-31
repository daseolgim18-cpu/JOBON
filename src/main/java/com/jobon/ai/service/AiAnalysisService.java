package com.jobon.ai.service;

/** [추가] 채용공고 분석/매칭/추천 서비스 */
import java.util.List;
import com.jobon.ai.vo.AiAnalysisVO;

public interface AiAnalysisService {
    List<AiAnalysisVO> list(Long memberId);

    AiAnalysisVO analyze(Long memberId, Long jobId);

    AiAnalysisVO get(Long memberId, Long analysisId);

    AiAnalysisVO getByJob(Long memberId, Long jobId);

    void saveRecommendation(Long memberId, Long recommendId, boolean saved);
}