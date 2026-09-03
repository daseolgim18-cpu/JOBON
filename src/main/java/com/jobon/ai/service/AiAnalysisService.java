package com.jobon.ai.service;

/** [추가] 채용공고 분석/매칭/추천 서비스 */
import java.util.List;
import com.jobon.ai.vo.AiAnalysisVO;
import com.jobon.ai.vo.AiExperienceRecommendVO;

public interface AiAnalysisService {
    List<AiAnalysisVO> list(Long memberId);

    AiAnalysisVO analyze(Long memberId, Long jobId);

    AiAnalysisVO get(Long memberId, Long analysisId);

    AiAnalysisVO getByJob(Long memberId, Long jobId);

    void saveRecommendation(Long memberId, Long recommendId, boolean saved);

    /** [추가] 로그인 회원이 저장한 자소서 경험 추천 목록을 조회합니다. */
    List<AiExperienceRecommendVO> savedRecommendations(Long memberId);

    /** [추가] 로그인 회원이 소유한 AI 분석 결과와 하위 기술/추천 데이터를 함께 삭제합니다. */
    void delete(Long memberId, Long analysisId);

    /** [추가] AI 분석 전에 부족한 입력 데이터를 사용자에게 안내합니다. */
    List<String> dataQualityWarnings(Long memberId, Long jobId);
}
