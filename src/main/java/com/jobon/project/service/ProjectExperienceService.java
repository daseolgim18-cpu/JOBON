package com.jobon.project.service;

/** [추가] 프로젝트 경험 서비스 */
import java.util.List;
import com.jobon.project.vo.ProjectExperienceVO;

public interface ProjectExperienceService {
    List<ProjectExperienceVO> list(Long memberId);

    ProjectExperienceVO get(Long memberId, Long id);

    void create(ProjectExperienceVO vo);

    void update(ProjectExperienceVO vo);

    void delete(Long memberId, Long id);
}