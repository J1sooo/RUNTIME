package com.est.runtime.signup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/index",
                                "/member/save",
                                "/api/member/save",
                                "/api/member/check-username",
                                "/h2-console/**",
                                "/css/**",
                                "/js/**" //
                        ).permitAll()
                        .requestMatchers("/post").access((authentication, context) -> {
                            if (context instanceof RequestAuthorizationContext cx) {
                                for (GrantedAuthority ga : authentication.get().getAuthorities()) {
                                    if (ga.getAuthority().equalsIgnoreCase(cx.getRequest().getMethod() + "_BOARD_" + cx.getRequest().getParameter("board"))) {
                                        return new AuthorizationDecision(true);
                                    }
                                }
                            }
                            return new AuthorizationDecision(false);
                        })
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
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