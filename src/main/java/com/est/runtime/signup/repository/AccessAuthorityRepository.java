package com.est.runtime.signup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.est.runtime.signup.entity.AccessAuthority;

@Repository
public interface AccessAuthorityRepository extends JpaRepository<AccessAuthority, Long> {
    Optional<AccessAuthority> findByName(String name);
    @Query(value = "SELECT a FROM AccessAuthority a INNER JOIN AuthorityForLevel l ON a.id = l.authority.id WHERE l.userLevel.id = :levelId")
    List<AccessAuthority> findAllByLevelId(@Param("levelId") Long levelId);
}
