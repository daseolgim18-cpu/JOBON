package com.jobon.apply.service;

/** [추가] Application 서비스 인터페이스 */
import java.util.List;
import com.jobon.apply.vo.ApplicationVO;

public interface ApplicationService {
    List<ApplicationVO> list(Long memberId, String keyword, String status, String sort);

    ApplicationVO get(Long memberId, Long applicationId);

    void create(ApplicationVO vo);

    void update(ApplicationVO vo);

    void delete(Long memberId, Long applicationId);
}
