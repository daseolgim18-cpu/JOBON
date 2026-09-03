package com.jobon.company.service;

/** [추가] Company CRUD 비즈니스 로직 */
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.company.dao.CompanyDAO;
import com.jobon.company.vo.CompanyVO;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyDAO dao;
    // [추가] 기업 CRUD 성공 시 ACTIVITY_LOG에 실제 활동 내역을 남깁니다.
    private final ActivityLogService activityLogService;

    public CompanyServiceImpl(CompanyDAO dao, ActivityLogService activityLogService) {
        this.dao = dao;
        this.activityLogService = activityLogService;
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
        // [추가] 기업 등록 성공 후 활동 내역 저장
        activityLogService.record(vo.getMemberId(), "COMPANY", "CREATE", vo.getCompanyId(),
                vo.getCompanyName() + " 기업 등록");
    }

    @Transactional
    public void update(CompanyVO vo) {
        validate(vo);
        if (dao.update(vo) != 1)
            throw new IllegalStateException("수정에 실패했습니다.");
        // [추가] 기업 수정 성공 후 활동 내역 저장
        activityLogService.record(vo.getMemberId(), "COMPANY", "UPDATE", vo.getCompanyId(),
                vo.getCompanyName() + " 기업 정보 수정");
    }

    @Transactional
    public void delete(Long memberId, Long companyId) {
        // [추가] 삭제 전에 제목으로 사용할 기업명을 조회합니다.
        CompanyVO existing = get(memberId, companyId);
        if (dao.delete(memberId, companyId) != 1)
            throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");
        activityLogService.record(memberId, "COMPANY", "DELETE", companyId,
                existing.getCompanyName() + " 기업 삭제");
    }

    private void validate(CompanyVO vo) {
        if (vo == null || vo.getMemberId() == null)
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        if (vo.getCompanyName() == null || vo.getCompanyName().isBlank())
            throw new IllegalArgumentException("기업명을 입력해주세요.");
        vo.setCompanyName(vo.getCompanyName().trim());
        if (vo.getCompanyName().length() > 150)
            throw new IllegalArgumentException("기업명은 150자 이하로 입력해주세요.");

        // [추가] COMPANY 컬럼 길이에 맞춰 저장 전에 공백/길이/URL 형식을 검증합니다.
        vo.setCompanyType(normalize(vo.getCompanyType()));
        vo.setIndustry(normalize(vo.getIndustry()));
        vo.setJobField(normalize(vo.getJobField()));
        vo.setBusinessType(normalize(vo.getBusinessType()));
        vo.setHomepageUrl(normalize(vo.getHomepageUrl()));
        vo.setAddress(normalize(vo.getAddress()));
        vo.setCareerUrl(normalize(vo.getCareerUrl()));
        vo.setLogoUrl(normalize(vo.getLogoUrl()));

        validateLength(vo.getCompanyType(), 50, "기업 구분");
        validateLength(vo.getIndustry(), 100, "산업");
        validateLength(vo.getJobField(), 100, "직무 분야");
        validateLength(vo.getBusinessType(), 150, "기업 업종");
        validateLength(vo.getHomepageUrl(), 500, "홈페이지 URL");
        validateLength(vo.getAddress(), 500, "주소");
        validateLength(vo.getCareerUrl(), 500, "채용 페이지 URL");
        validateLength(vo.getLogoUrl(), 500, "로고 URL");
        validateHttpUrl(vo.getHomepageUrl(), "홈페이지 URL");
        validateHttpUrl(vo.getCareerUrl(), "채용 페이지 URL");
        validateHttpUrl(vo.getLogoUrl(), "로고 URL");
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private void validateLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength)
            throw new IllegalArgumentException(fieldName + "은(는) " + maxLength + "자 이하로 입력해주세요.");
    }

    private void validateHttpUrl(String value, String fieldName) {
        if (value != null && !(value.startsWith("http://") || value.startsWith("https://")))
            throw new IllegalArgumentException(fieldName + "은(는) http:// 또는 https://로 시작해야 합니다.");
    }
}
