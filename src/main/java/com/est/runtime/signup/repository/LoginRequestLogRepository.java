package com.est.runtime.signup.repository;


import com.est.runtime.signup.entity.LoginRequestLog;
import com.est.runtime.signup.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoginRequestLogRepository extends JpaRepository<LoginRequestLog, Long> {
    List<LoginRequestLog> findAllByMember(Member member);
    List<LoginRequestLog> findAllByMemberAndLoginTimeBetween(Member member, LocalDateTime before, LocalDateTime after);
    List<LoginRequestLog> findAllByMemberAndLoginTimeBefore(Member member, LocalDateTime before);
    List<LoginRequestLog> findAllByMemberAndLoginTimeAfter(Member member, LocalDateTime after);
    Optional<LoginRequestLog> findById(Long id);

    void deleteByMember(Member member);
}
