package com.est.runtime.signup.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.est.runtime.signup.entity.LoginRequestLog;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.repository.LoginRequestLogRepository;
import com.est.runtime.signup.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoggingService {
    private final LoginRequestLogRepository logRepository;
    private final MemberRepository memberRepository;

    public void onLoginAttempt(HttpServletRequest request, boolean isSuccessful) {
        Optional<Member> memberQuery = memberRepository.findByUsername(request.getParameter("username"));
        if (memberQuery.isEmpty()) {
            return;
        }
        Member memberGet = memberQuery.get();
        if (isSuccessful){
            memberGet.resetFailureCount();
            memberGet = memberRepository.save(memberGet);
        } else {
            memberGet.incrementFailureCount();
            memberGet = memberRepository.save(memberGet);
        }

        logRepository.save(LoginRequestLog.builder().
            isSuccessful(isSuccessful).
            ipAddress(request.getRemoteAddr()).
            loginTime(LocalDateTime.now()).
            userAgent(request.getHeader("User-Agent")).
            member(memberGet).
            build());
    }

}
