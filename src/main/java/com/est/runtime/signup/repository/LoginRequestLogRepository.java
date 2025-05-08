package com.est.runtime.signup.repository;

import com.est.runtime.signup.entity.LoginRequestLog;
import com.est.runtime.signup.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoginRequestLogRepository extends JpaRepository<LoginRequestLog, Long> {
    List<LoginRequestLog> findByMember(Member member);
    Optional<LoginRequestLog> findById(Long id);

    void deleteByMember(Member member);
}