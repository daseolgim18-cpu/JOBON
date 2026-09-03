package com.jobon.savedsearch.service;

/** 저장 검색 조건 조회/등록/삭제 비즈니스 로직 */
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobon.savedsearch.dao.SavedSearchDAO;
import com.jobon.savedsearch.vo.SavedSearchVO;

@Service
public class SavedSearchServiceImpl implements SavedSearchService {
    private static final Set<String> TARGET_TYPES = Set.of("ALL", "COMPANY", "JOB");
    private static final Set<String> CAREER_TYPES = Set.of("NEW", "CAREER", "INTERN", "");

    private final SavedSearchDAO dao;

    public SavedSearchServiceImpl(SavedSearchDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<SavedSearchVO> list(Long memberId) {
        return dao.selectList(memberId);
    }

    @Override
    public SavedSearchVO get(Long memberId, Long searchId) {
        SavedSearchVO vo = dao.selectOne(memberId, searchId);
        if (vo == null) throw new IllegalArgumentException("저장된 검색어를 찾을 수 없습니다.");
        return vo;
    }

    @Override
    @Transactional
    public void create(SavedSearchVO vo) {
        validate(vo);
        trim(vo);
        if (dao.insert(vo) != 1) throw new IllegalStateException("검색 조건을 저장하지 못했습니다.");
    }

    @Override
    @Transactional
    public void delete(Long memberId, Long searchId) {
        if (dao.delete(memberId, searchId) != 1) {
            throw new IllegalArgumentException("삭제할 저장 검색어를 찾을 수 없습니다.");
        }
    }

    private void validate(SavedSearchVO vo) {
        if (vo == null || vo.getMemberId() == null) throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getSearchName() == null || vo.getSearchName().isBlank()) throw new IllegalArgumentException("검색어 이름을 입력해주세요.");
        if (vo.getSearchName().trim().length() > 100) throw new IllegalArgumentException("검색어 이름은 100자 이하로 입력해주세요.");
        String target = vo.getTargetType() == null ? "ALL" : vo.getTargetType().trim().toUpperCase();
        if (!TARGET_TYPES.contains(target)) throw new IllegalArgumentException("검색 대상 값이 올바르지 않습니다.");
        vo.setTargetType(target);
        String career = vo.getCareerType() == null ? "" : vo.getCareerType().trim().toUpperCase();
        if (!CAREER_TYPES.contains(career)) throw new IllegalArgumentException("경력 조건 값이 올바르지 않습니다.");
        vo.setCareerType(career.isBlank() ? null : career);
        if (vo.getPostedFrom() != null && vo.getPostedTo() != null && vo.getPostedFrom().isAfter(vo.getPostedTo())) {
            throw new IllegalArgumentException("등록일 시작일은 종료일보다 늦을 수 없습니다.");
        }
        if (vo.getDeadlineFrom() != null && vo.getDeadlineTo() != null && vo.getDeadlineFrom().isAfter(vo.getDeadlineTo())) {
            throw new IllegalArgumentException("마감일 시작일은 종료일보다 늦을 수 없습니다.");
        }
    }

    private void trim(SavedSearchVO vo) {
        vo.setSearchName(trimToNull(vo.getSearchName()));
        vo.setKeyword(trimToNull(vo.getKeyword()));
        vo.setJobRole(trimToNull(vo.getJobRole()));
        vo.setRegion(trimToNull(vo.getRegion()));
        vo.setExtraConditions(trimToNull(vo.getExtraConditions()));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
