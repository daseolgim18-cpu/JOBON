package com.jobon.savedsearch.service;

import java.util.List;

import com.jobon.savedsearch.vo.SavedSearchVO;

public interface SavedSearchService {
    List<SavedSearchVO> list(Long memberId);

    SavedSearchVO get(Long memberId, Long searchId);

    void create(SavedSearchVO vo);

    void delete(Long memberId, Long searchId);
}
