package com.jobon.project.dao;

/** [추가] 프로젝트 경험/기능/트러블슈팅/기술 DAO */
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.jobon.project.vo.*;

@Mapper
public interface ProjectExperienceDAO {
    List<ProjectExperienceVO> selectList(@Param("memberId") Long memberId);

    ProjectExperienceVO selectOne(@Param("memberId") Long memberId, @Param("projectId") Long projectId);

    int insert(ProjectExperienceVO vo);

    int update(ProjectExperienceVO vo);

    int delete(@Param("memberId") Long memberId, @Param("projectId") Long projectId);

    void mergeTech(@Param("techName") String techName);

    Long selectTechId(@Param("techName") String techName);

    int insertProjectTech(@Param("projectId") Long projectId, @Param("techId") Long techId);

    int deleteProjectTech(@Param("projectId") Long projectId);

    List<String> selectTechNames(@Param("projectId") Long projectId);

    int insertFeature(ProjectFeatureVO vo);

    int deleteFeatures(@Param("projectId") Long projectId);

    List<ProjectFeatureVO> selectFeatures(@Param("projectId") Long projectId);

    int insertTrouble(ProjectTroubleVO vo);

    int deleteTroubles(@Param("projectId") Long projectId);

    List<ProjectTroubleVO> selectTroubles(@Param("projectId") Long projectId);
}
