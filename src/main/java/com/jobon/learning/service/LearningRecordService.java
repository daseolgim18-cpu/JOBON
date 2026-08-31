package com.jobon.learning.service;

/** [추가] 성장 기록 서비스 */
import java.util.List;
import com.jobon.learning.vo.LearningRecordVO;

public interface LearningRecordService {
    List<LearningRecordVO> list(Long memberId);

    LearningRecordVO get(Long memberId, Long id);

    void create(LearningRecordVO vo);

    void update(LearningRecordVO vo);

    void delete(Long memberId, Long id);
}