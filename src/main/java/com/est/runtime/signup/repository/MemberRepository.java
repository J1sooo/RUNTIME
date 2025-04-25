package com.est.runtime.signup.repository;

import com.est.runtime.signup.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
