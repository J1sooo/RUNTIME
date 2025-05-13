package com.est.runtime.signup.interceptors;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.est.runtime.signup.service.LoggingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginRequestLoggingSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final LoggingService loggingService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                loggingService.onLoginAttempt(request, true);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public LoginRequestLoggingSuccessHandler(String successUrl, boolean alwaysUse, LoggingService loggingService) {
        super();
        this.setDefaultTargetUrl(successUrl);
        this.setAlwaysUseDefaultTargetUrl(alwaysUse);
        this.loggingService = loggingService;
    }
}
