package com.jobon.common.config;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * Spring Security 공통 설정입니다.
 * JOBON은 일반 로그인 상태를 HttpSession + MVC Interceptor로 검사하므로,
 * Spring Security의 기본 로그인 화면과 자동 인증 처리는 끄고 BCrypt PasswordEncoder만 사용합니다.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        // 회원가입 시 비밀번호를 암호화하고 로그인 시 matches()로 비교할 때 사용하는 BCrypt 객체
        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // Spring Security의 기본 로그인 화면은 사용하지 않고 JOBON 자체 세션 로그인 흐름을 사용한다.
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // REST 방식의 회원가입/로그인 API를 사용하므로 현재 개발 단계에서는 CSRF를 비활성화한다.
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .logout(AbstractHttpConfigurer::disable);
                return http.build();
        }
}