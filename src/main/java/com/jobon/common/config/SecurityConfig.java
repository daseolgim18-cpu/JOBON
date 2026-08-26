package com.jobon.common.config;

import jakarta.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests(auth -> auth

                                                // JSP 내부 forward / 에러 처리 허용
                                                .dispatcherTypeMatchers(
                                                                DispatcherType.FORWARD,
                                                                DispatcherType.ERROR)
                                                .permitAll()

                                                // 비로그인 사용자도 접근 가능한 경로
                                                .requestMatchers(
                                                                "/",
                                                                "/main",
                                                                "/login",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**")
                                                .permitAll()

                                                // 나머지는 로그인 필요
                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutSuccessUrl("/main"));

                return http.build();
        }
}