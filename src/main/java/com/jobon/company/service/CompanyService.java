package com.jobon.company.service;

/** [추가] Company 서비스 인터페이스 */
import java.util.List;
import com.jobon.company.vo.CompanyVO;

public interface CompanyService {
    List<CompanyVO> list(Long memberId, String keyword, String companyType);

    CompanyVO get(Long memberId, Long companyId);

    void create(CompanyVO vo);

    void update(CompanyVO vo);

    void delete(Long memberId, Long companyId);
}
