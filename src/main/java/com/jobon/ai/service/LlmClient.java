package com.jobon.ai.service;

import com.jobon.ai.dto.*;

/** [추가] LLM Provider 교체가 가능하도록 분리한 인터페이스 */
public interface LlmClient {
    LlmAnalysisResponse analyze(LlmAnalysisRequest request);
}