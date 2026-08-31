package com.jobon.apply.service;

/** [추가] Application CRUD 비즈니스 로직 */
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.apply.dao.ApplicationDAO;
import com.jobon.apply.vo.ApplicationVO;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationDAO dao;

    public ApplicationServiceImpl(ApplicationDAO dao) {
        this.dao = dao;
    }

    public List<ApplicationVO> list(Long memberId, String keyword, String status, String sort) {
        return dao.selectList(memberId, keyword, status, sort);
    }

    public ApplicationVO get(Long memberId, Long applicationId) {
        ApplicationVO v = dao.selectOne(memberId, applicationId);
        if (v == null)
            throw new IllegalArgumentException("데이터를 찾을 수 없습니다.");
        return v;
    }

    @Transactional
    public void create(ApplicationVO vo) {
        validate(vo);
        if (dao.insert(vo) != 1)
            throw new IllegalStateException("등록에 실패했습니다.");
    }

    @Transactional
    public void update(ApplicationVO vo) {
        validate(vo);
        if (dao.update(vo) != 1)
            throw new IllegalStateException("수정에 실패했습니다.");
    }

    @Transactional
    public void delete(Long memberId, Long applicationId) {
        if (dao.delete(memberId, applicationId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
    }

    private void validate(ApplicationVO vo) {
        if (vo == null || vo.getMemberId() == null)
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getJobId() == null)
            throw new IllegalArgumentException("채용공고를 선택해주세요.");
        if (vo.getStatus() == null || vo.getStatus().isBlank())
            vo.setStatus("INTEREST");
    }
}
