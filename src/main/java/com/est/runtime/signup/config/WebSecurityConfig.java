package com.est.runtime.signup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.est.runtime.signup.interceptors.LoginRequestLoggingFailureHandler;
import com.est.runtime.signup.interceptors.LoginRequestLoggingSuccessHandler;
import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurityConfig {
    private final LoginRequestLoggingFailureHandler loginFailureHandler;
    private final LoginRequestLoggingSuccessHandler loginSuccessHandler;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl handler = new AccessDeniedHandlerImpl();
        handler.setErrorPage("/denied");
        return handler;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/index",
                                "/login",
                                "/login?error",
                                "/member/save",
                                "/api/member/login-status",
                                "/api/member/save",
                                "/api/member/check-username",
                                "/api/member/check-nickname",
                                "/h2-console/**",
                                "/css/**",
                                "/js/**",
                                "/api/board/**",
                                "/crew"// 나중에 등급별 생기면 삭제

                        ).permitAll()
                        .requestMatchers("/post").access((authentication, context) -> {
                            if (context instanceof RequestAuthorizationContext cx) {
                                String boardId = cx.getRequest().getParameter("board");
                                if (boardId.equalsIgnoreCase("1") || boardId.equalsIgnoreCase("2") || boardId.equalsIgnoreCase("3")) {
                                    return new AuthorizationDecision(true);
                                }
                                for (GrantedAuthority ga : authentication.get().getAuthorities()) {
                                    if (ga.getAuthority().equalsIgnoreCase("RUNTIME_ADMIN")) {
                                        return new AuthorizationDecision(true);
                                    }
                                    if (ga.getAuthority().equalsIgnoreCase(cx.getRequest().getMethod() + "_BOARD_" + cx.getRequest().getParameter("board"))) {
                                        return new AuthorizationDecision(true);
                                    }
                                }
                            }
                            return new AuthorizationDecision(false);
                        })
                        .requestMatchers("/api/admin/get-members",
                            "/api/admin/is-admin",
                            "/api/admin/request-admin").authenticated()
                        .requestMatchers("/post/**").access((authentication, context) -> {
                            if (context instanceof RequestAuthorizationContext cx) {
                                for (GrantedAuthority ga : authentication.get().getAuthorities()) {
                                    if (ga.getAuthority().equalsIgnoreCase("RUNTIME_ADMIN")) {
                                        return new AuthorizationDecision(true);
                                    }
                                    if (ga.getAuthority().equalsIgnoreCase(cx.getRequest().getMethod() + "_BOARD_" + cx.getRequest().getParameter("board"))) {
                                        return new AuthorizationDecision(true);
                                    }
                                }
                            }
                            return new AuthorizationDecision(false);
                        })
                        .requestMatchers("/api/admin/**", "/adminPage").access((authentication, context) -> {
                            if (context instanceof RequestAuthorizationContext cx) {
                                for (GrantedAuthority ga : authentication.get().getAuthorities()) {
                                    if (ga.getAuthority().equalsIgnoreCase("RUNTIME_ADMIN")) {
                                        return new AuthorizationDecision(true);
                                    }
                                }
                            }
                            return new AuthorizationDecision(false);
                        })
                        .requestMatchers("/notes").access((authentication, context) -> {
                            if (context instanceof RequestAuthorizationContext cx) {
                                for (GrantedAuthority ga : authentication.get().getAuthorities()) {
                                    if (ga.getAuthority().equalsIgnoreCase("RUNTIME_ADMIN") || ga.getAuthority().equalsIgnoreCase("ACCESS_NOTES")) {
                                        return new AuthorizationDecision(true);
                                    }
                                }
                            }
                            return new AuthorizationDecision(false);
                        })
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .formLogin(auth -> auth
                        .loginPage("/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                )
                .logout(auth -> auth
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                );
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}