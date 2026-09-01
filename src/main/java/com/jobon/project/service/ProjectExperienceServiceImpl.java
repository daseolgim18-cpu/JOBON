package com.jobon.project.service;

/** [추가] 프로젝트 CRUD + 기술/담당기능/트러블슈팅을 하나의 트랜잭션으로 관리 */
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.project.dao.ProjectExperienceDAO;
import com.jobon.project.vo.*;

@Service
public class ProjectExperienceServiceImpl implements ProjectExperienceService {
    private final ProjectExperienceDAO dao;
    // [추가] 프로젝트 CRUD 성공 시 실제 활동 내역을 저장합니다.
    private final ActivityLogService activityLogService;

    public ProjectExperienceServiceImpl(ProjectExperienceDAO dao, ActivityLogService activityLogService) {
        this.dao = dao;
        this.activityLogService = activityLogService;
    }

    public List<ProjectExperienceVO> list(Long memberId) {
        List<ProjectExperienceVO> l = dao.selectList(memberId);
        l.forEach(this::fill);
        return l;
    }

    public ProjectExperienceVO get(Long memberId, Long id) {
        ProjectExperienceVO v = dao.selectOne(memberId, id);
        if (v == null)
            throw new IllegalArgumentException("프로젝트를 찾을 수 없습니다.");
        fill(v);
        return v;
    }

    @Transactional
    public void create(ProjectExperienceVO v) {
        validate(v);
        dao.insert(v);
        sync(v);
        // [추가] 프로젝트/기술/기능/트러블슈팅 저장이 모두 성공한 뒤 활동 내역 저장
        activityLogService.record(v.getMemberId(), "PROJECT", "CREATE", v.getProjectId(),
                v.getProjectName() + " 프로젝트 등록");
    }

    @Transactional
    public void update(ProjectExperienceVO v) {
        validate(v);
        if (dao.update(v) != 1)
            throw new IllegalArgumentException("프로젝트를 찾을 수 없습니다.");
        sync(v);
        // [추가] 프로젝트/기술/기능/트러블슈팅 수정이 모두 성공한 뒤 활동 내역 저장
        activityLogService.record(v.getMemberId(), "PROJECT", "UPDATE", v.getProjectId(),
                v.getProjectName() + " 프로젝트 수정");
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        // [추가] 삭제 전에 활동 제목으로 사용할 프로젝트명을 조회합니다.
        ProjectExperienceVO existing = get(memberId, id);
        if (dao.delete(memberId, id) != 1)
            throw new IllegalArgumentException("프로젝트를 찾을 수 없습니다.");
        activityLogService.record(memberId, "PROJECT", "DELETE", id,
                existing.getProjectName() + " 프로젝트 삭제");
    }

    private void validate(ProjectExperienceVO v) {
        if (v.getProjectName() == null || v.getProjectName().isBlank())
            throw new IllegalArgumentException("프로젝트명을 입력해주세요.");
        if (v.getDescription() == null || v.getDescription().isBlank())
            throw new IllegalArgumentException("프로젝트 내용과 수행 경험을 입력해주세요.");
    }

    private void fill(ProjectExperienceVO v) {
        v.setTechNames(String.join(", ", dao.selectTechNames(v.getProjectId())));
        v.setFeatures(dao.selectFeatures(v.getProjectId()));
        v.setTroubles(dao.selectTroubles(v.getProjectId()));
    }

    private void sync(ProjectExperienceVO v) {
        dao.deleteProjectTech(v.getProjectId());
        for (String n : split(v.getTechNames())) {
            dao.mergeTech(n);
            dao.insertProjectTech(v.getProjectId(), dao.selectTechId(n));
        }
        dao.deleteFeatures(v.getProjectId());
        int i = 1;
        for (ProjectFeatureVO f : v.getFeatures()) {
            if (f.getFeatureName() == null || f.getFeatureName().isBlank())
                continue;
            f.setProjectId(v.getProjectId());
            f.setSortOrder(i++);
            dao.insertFeature(f);
        }
        dao.deleteTroubles(v.getProjectId());
        for (ProjectTroubleVO t : v.getTroubles()) {
            if (t.getTitle() == null || t.getTitle().isBlank())
                continue;
            t.setProjectId(v.getProjectId());
            dao.insertTrouble(t);
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
