package com.jobon.common.config;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * Spring MVC 설정 파일입니다.
 * LoginCheckInterceptor를 등록하여 로그인 후에만 접근해야 하는 URL을 한곳에서 관리합니다.
 */
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.jobon.common.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 실제 세션 로그인 여부를 검사하는 Interceptor
    private final LoginCheckInterceptor loginCheckInterceptor;

    // [수정] MypageController와 동일한 프로필 이미지 저장 경로를 사용합니다.
    @Value("${jobon.upload.profile-dir:uploads/profiles}")
    private String profileUploadDir;

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

    @Override
    // [수정] 로컬에 저장된 프로필 이미지를 /uploads/profiles/** URL로 제공한다.
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Paths.get(profileUploadDir).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/uploads/profiles/**")
                .addResourceLocations(location.endsWith("/") ? location : location + "/");
    }
}