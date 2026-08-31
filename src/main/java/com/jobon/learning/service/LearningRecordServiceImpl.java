package com.jobon.learning.service;

/** [추가] 성장 기록 CRUD와 TECH_STACK/LEARNING_TECH 동기화 */
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.learning.dao.LearningRecordDAO;
import com.jobon.learning.vo.LearningRecordVO;

@Service
public class LearningRecordServiceImpl implements LearningRecordService {
    private final LearningRecordDAO dao;

    public LearningRecordServiceImpl(LearningRecordDAO dao) {
        this.dao = dao;
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
    }

    @Transactional
    public void update(LearningRecordVO v) {
        validate(v);
        if (dao.update(v) != 1)
            throw new IllegalArgumentException("성장 기록을 찾을 수 없습니다.");
        syncTech(v);
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        if (dao.delete(memberId, id) != 1)
            throw new IllegalArgumentException("성장 기록을 찾을 수 없습니다.");
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
