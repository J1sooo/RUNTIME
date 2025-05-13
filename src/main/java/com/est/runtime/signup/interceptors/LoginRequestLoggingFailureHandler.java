package com.est.runtime.signup.interceptors;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.est.runtime.signup.service.LoggingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginRequestLoggingFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final LoggingService loggingService;

    public LoginRequestLoggingFailureHandler(LoggingService loggingService, String failureUrl) {
        super(failureUrl);
        this.loggingService = loggingService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
                loggingService.onLoginAttempt(request, false);
        super.onAuthenticationFailure(request, response, exception);
    }

}
