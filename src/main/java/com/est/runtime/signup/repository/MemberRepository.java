package com.est.runtime.signup.repository;

import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.entity.UserLevel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNickname(String nickname);
    List<Member> findByLevel(UserLevel level);
    boolean existsByUsername(String username);

    @Query(value = "SELECT m FROM Member m WHERE m.nickname LIKE '%' || :nickname || '%' AND m.username LIKE '%' || :username || '%'" )
    List<Member> searchMembers(@Param("nickname") String nickname, @Param("username") String username);
}
