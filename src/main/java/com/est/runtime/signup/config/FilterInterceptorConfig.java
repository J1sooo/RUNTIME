package com.est.runtime.signup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.est.runtime.signup.interceptors.LoginRequestLoggingFailureHandler;
import com.est.runtime.signup.interceptors.LoginRequestLoggingSuccessHandler;
import com.est.runtime.signup.service.LoggingService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class FilterInterceptorConfig implements WebMvcConfigurer {
    private final LoggingService loggingService;

    @Bean
    public LoginRequestLoggingFailureHandler loginFailureHandler() {
        return new LoginRequestLoggingFailureHandler(loggingService, "/login?error");
    }

    @Bean
    public LoginRequestLoggingSuccessHandler loginSuccessHandler() {
        return new LoginRequestLoggingSuccessHandler("/index", true, loggingService);
    }

}