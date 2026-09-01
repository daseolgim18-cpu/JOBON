package com.jobon.learning.service;

/** [추가] 성장 기록 CRUD와 TECH_STACK/LEARNING_TECH 동기화 */
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.learning.dao.LearningRecordDAO;
import com.jobon.learning.vo.LearningRecordVO;

@Service
public class LearningRecordServiceImpl implements LearningRecordService {
    private final LearningRecordDAO dao;
    // [추가] 성장 기록 CRUD 성공 시 실제 활동 내역을 저장합니다.
    private final ActivityLogService activityLogService;

    public LearningRecordServiceImpl(LearningRecordDAO dao, ActivityLogService activityLogService) {
        this.dao = dao;
        this.activityLogService = activityLogService;
    }

    public List<LearningRecordVO> list(Long memberId) {
        List<LearningRecordVO> list = dao.selectList(memberId);
        list.forEach(this::fillTech);
        return list;
    }

    public LearningRecordVO get(Long memberId, Long id) {
        LearningRecordVO v = dao.selectOne(memberId, id);
        if (v == null)
            throw new IllegalArgumentException("성장 기록을 찾을 수 없습니다.");
        fillTech(v);
        return v;
    }

    @Transactional
    public void create(LearningRecordVO v) {
        validate(v);
        dao.insert(v);
        syncTech(v);
        // [추가] 성장 기록과 기술 연결 저장이 모두 성공한 뒤 활동 내역 저장
        activityLogService.record(v.getMemberId(), "LEARNING", "CREATE", v.getLearningId(),
                v.getSubject() + " 성장 기록 등록");
    }

    @Transactional
    public void update(LearningRecordVO v) {
        validate(v);
        if (dao.update(v) != 1)
            throw new IllegalArgumentException("성장 기록을 찾을 수 없습니다.");
        syncTech(v);
        // [추가] 성장 기록과 기술 연결 수정이 모두 성공한 뒤 활동 내역 저장
        activityLogService.record(v.getMemberId(), "LEARNING", "UPDATE", v.getLearningId(),
                v.getSubject() + " 성장 기록 수정");
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        // [추가] 삭제 전에 활동 제목으로 사용할 성장 기록 주제를 조회합니다.
        LearningRecordVO existing = get(memberId, id);
        if (dao.delete(memberId, id) != 1)
            throw new IllegalArgumentException("성장 기록을 찾을 수 없습니다.");
        activityLogService.record(memberId, "LEARNING", "DELETE", id,
                existing.getSubject() + " 성장 기록 삭제");
    }

    private void validate(LearningRecordVO v) {
        if (v.getSubject() == null || v.getSubject().isBlank())
            throw new IllegalArgumentException("주제를 입력해주세요.");
        if (v.getRecordType() == null || v.getRecordType().isBlank())
            v.setRecordType("LEARNING");
    }

    private void fillTech(LearningRecordVO v) {
        v.setTechNames(String.join(", ", dao.selectTechNames(v.getLearningId())));
    }

    private void syncTech(LearningRecordVO v) {
        dao.deleteLearningTech(v.getLearningId());
        for (String n : split(v.getTechNames())) {
            dao.mergeTech(n);
            dao.insertLearningTech(v.getLearningId(), dao.selectTechId(n));
        }
    }

    private Set<String> split(String s) {
        Set<String> r = new LinkedHashSet<>();
        if (s != null)
            for (String x : s.split("[,\n]")) {
                String n = x.trim();
                if (!n.isEmpty())
                    r.add(n);
            }
        return r;
    }
}
