package com.jobon.company.service;

/** [추가] Company CRUD 비즈니스 로직 */
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.company.dao.CompanyDAO;
import com.jobon.company.vo.CompanyVO;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyDAO dao;

    public CompanyServiceImpl(CompanyDAO dao) {
        this.dao = dao;
    }

    public List<CompanyVO> list(Long memberId, String keyword, String companyType) {
        return dao.selectList(memberId, keyword, companyType);
    }

    public CompanyVO get(Long memberId, Long companyId) {
        CompanyVO v = dao.selectOne(memberId, companyId);
        if (v == null)
            throw new IllegalArgumentException("데이터를 찾을 수 없습니다.");
        return v;
    }

    @Transactional
    public void create(CompanyVO vo) {
        validate(vo);
        if (dao.insert(vo) != 1)
            throw new IllegalStateException("등록에 실패했습니다.");
    }

    @Transactional
    public void update(CompanyVO vo) {
        validate(vo);
        if (dao.update(vo) != 1)
            throw new IllegalStateException("수정에 실패했습니다.");
    }

    @Transactional
    public void delete(Long memberId, Long companyId) {
        if (dao.delete(memberId, companyId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
    }

    private void validate(CompanyVO vo) {
        if (vo == null || vo.getMemberId() == null)
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getCompanyName() == null || vo.getCompanyName().isBlank())
            throw new IllegalArgumentException("기업명을 입력해주세요.");
    }
}
