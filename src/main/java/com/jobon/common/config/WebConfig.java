package com.jobon.common.config;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * Spring MVC 설정 파일입니다.
 * LoginCheckInterceptor를 등록하여 로그인 후에만 접근해야 하는 URL을 한곳에서 관리합니다.
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.jobon.common.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 실제 세션 로그인 여부를 검사하는 Interceptor
    private final LoginCheckInterceptor loginCheckInterceptor;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor) {
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    @Override
    // 로그인 후에만 접근 가능한 JOBON 주요 메뉴 URL에 Interceptor를 적용한다.
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/dashboard/**", "/company/**", "/job/**", "/apply/**", "/todo/**", "/learning/**",
                        "/project/**", "/ai/**", "/mypage/**", "/search/**")
                .excludePathPatterns("/job/public/**");
    }
}