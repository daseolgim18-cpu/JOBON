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

    /** [추가] 로그인 회원이 소유한 AI 분석 결과와 하위 기술/추천 데이터를 함께 삭제합니다. */
    void delete(Long memberId, Long analysisId);
}
